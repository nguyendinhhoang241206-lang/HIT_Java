#include <ai/Game.h>
#include <ai/AI.h>
#include <time.h>
#include <vector>
#include <queue>
#include <cmath>
#include <algorithm>
#include <cstdio>

using namespace std;

// =========================================================================
// CẤU HÌNH & HẰNG SỐ
// =========================================================================
#define DIR_UP 1
#define DIR_RIGHT 2
#define DIR_DOWN 3
#define DIR_LEFT 4

// COST CHO A* (Mô phỏng tư duy con người)
#define COST_GROUND 1.0f
#define COST_SOFT_WALL 15.0f // Phá tường tương đương đi 15 ô (JS logic)
#define COST_DANGER 5000.0f  // Vùng chết

// Cờ & Cache
bool g_placed[MAP_W][MAP_H];
float g_riskMap[MAP_W][MAP_H]; // Dùng float để map nhiệt mịn hơn
int g_stuckCount[NUMBER_OF_TANK];
float g_lastX[NUMBER_OF_TANK], g_lastY[NUMBER_OF_TANK];

// Target (Cache mục tiêu)
int g_targetX[NUMBER_OF_TANK];
int g_targetY[NUMBER_OF_TANK];
bool g_hasTarget[NUMBER_OF_TANK];

// Node cho A*
struct Node {
    int x, y;
    float g, h; // Cost dùng float để chính xác
    int parentDir;

    // So sánh để priority_queue đẩy thằng có F = G+H nhỏ nhất lên đầu
    bool operator>(const Node& other) const {
        return (g + h) > (other.g + other.h);
    }
};

// =========================================================================
// HÀM HỖ TRỢ TOÁN HỌC (Float Precision)
// =========================================================================
int Round(float x) { return (int)(x + 0.5f); }
bool IsValid(int x, int y) { return x >= 0 && x < MAP_W&& y >= 0 && y < MAP_H; }
float GetDist(float x1, float y1, float x2, float y2) { return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2)); }
float GetManhattan(float x1, float y1, float x2, float y2) { return abs(x1 - x2) + abs(y1 - y2); }

// =========================================================================
// HỆ THỐNG CẢM BIẾN (SENSORS)
// =========================================================================

// Kiểm tra có phải nhà mình không (Bảo vệ tuyệt đối)
bool IsMyBase(int x, int y) {
    AI* p = AI::GetInstance();
    for (auto b : p->GetMyBases())
        if (b->GetX() == x && b->GetY() == y && b->GetHP() > 0) return true;

    // Fallback nếu server chưa gửi list base
    if (p->GetBlock(x, y) == BLOCK_BASE) {
        if (p->GetMyTeam() == TEAM_1 && (x < 5 || y < 5)) return true;
        if (p->GetMyTeam() == TEAM_2 && (x > 16 || y > 16)) return true;
    }
    return false;
}

// Kiểm tra vật cản động (Tank)
bool IsOccupied(int x, int y, int myID) {
    AI* p = AI::GetInstance();
    for (int i = 0; i < 4; i++) {
        if (i != myID) {
            Tank* t = p->GetMyTank(i);
            if (t && t->GetHP() > 0 && Round(t->GetX()) == x && Round(t->GetY()) == y) return true;
        }
        Tank* e = p->GetEnemyTank(i);
        if (e && e->GetHP() > 0 && Round(e->GetX()) == x && Round(e->GetY()) == y) return true;
    }
    return false;
}

// =========================================================================
// 1. RISK MAPPING (BẢN ĐỒ NGUY HIỂM)
// =========================================================================
void BuildRiskMap() {
    for (int i = 0; i < MAP_W; i++) for (int j = 0; j < MAP_H; j++) g_riskMap[i][j] = 0;
    AI* p = AI::GetInstance();

    // Bom (Strike)
    for (auto s : p->GetIncomingStrike()) {
        int sx = s->GetX(), sy = s->GetY();
        for (int dx = -1; dx <= 1; dx++) for (int dy = -1; dy <= 1; dy++)
            if (IsValid(sx + dx, sy + dy)) g_riskMap[sx + dx][sy + dy] = COST_DANGER;
    }

    // Đạn (Bullet) - Dự đoán đường đạn bay 6 ô
    for (auto b : p->GetEnemyBullets()) {
        if (!b->IsAlive()) continue;
        int x = Round(b->GetX());
        int y = Round(b->GetY());
        int dir = b->GetDirection();

        for (int k = 0; k < 6; k++) {
            if (IsValid(x, y)) g_riskMap[x][y] += COST_DANGER;
            if (dir == DIR_UP) y--; else if (dir == DIR_RIGHT) x++;
            else if (dir == DIR_DOWN) y++; else x--;

            if (!IsValid(x, y) || p->GetBlock(x, y) == BLOCK_HARD_OBSTACLE) break;
        }
    }
}

// =========================================================================
// 2. RAYCASTING VISION (Sao chép logic CanISee của JS)
// =========================================================================
// 0: Không bắn được, 1: Bắn Địch, 2: Bắn Tường
int RaycastCheck(int myID, int x, int y, int tx, int ty) {
    if (x != tx && y != ty) return 0; // Không thẳng hàng

    int dist = abs(x - tx) + abs(y - ty);
    int dx = (tx > x) ? 1 : (tx < x) ? -1 : 0;
    int dy = (ty > y) ? 1 : (ty < y) ? -1 : 0;

    for (int i = 1; i < dist; i++) {
        int cx = x + dx * i;
        int cy = y + dy * i;
        int blk = AI::GetInstance()->GetBlock(cx, cy);

        // Gặp Tường Cứng -> Chặn
        if (blk == BLOCK_HARD_OBSTACLE) return 0;

        // Gặp Nhà -> Chỉ bắn nếu đó là mục tiêu (Nhà Địch) và không phải Nhà Mình
        if (blk == BLOCK_BASE) {
            if (IsMyBase(cx, cy)) return 0; // Nhà mình -> STOP
            if (cx != tx || cy != ty) return 0; // Nhà địch chắn đường -> STOP
        }

        // Gặp Đồng Đội -> STOP
        if (IsOccupied(cx, cy, myID)) return 0;

        // Gặp Tường Mềm -> Bắn Phá (Return 2)
        if (blk == BLOCK_SOFT_OBSTACLE) return 2;
    }
    return 1; // Clear shot
}

// =========================================================================
// 3. A* PATHFINDING (THUẬT TOÁN TÌM ĐƯỜNG CAO CẤP)
// =========================================================================
int GetMoveAStar(int sx, int sy, int ex, int ey, int myID) {
    if (sx == ex && sy == ey) return 0;

    priority_queue<Node, vector<Node>, greater<Node> > pq;
    float gScore[MAP_W][MAP_H];
    int firstDir[MAP_W][MAP_H];

    for (int i = 0; i < MAP_W; i++) for (int j = 0; j < MAP_H; j++) gScore[i][j] = 999999.0f;

    gScore[sx][sy] = 0;
    pq.push({ sx, sy, 0, GetManhattan(sx, sy, ex, ey), 0 });

    while (!pq.empty()) {
        Node u = pq.top(); pq.pop();

        if (u.g > gScore[u.x][u.y]) continue;
        if (u.x == ex && u.y == ey) return firstDir[u.x][u.y];

        int dirs[] = { 1, 2, 3, 4 };
        for (int k = 0; k < 4; k++) { int r = rand() % 4; swap(dirs[k], dirs[r]); }

        for (int dir : dirs) {
            int nx = u.x, ny = u.y;
            if (dir == 1) ny--; else if (dir == 2) nx++; else if (dir == 3) ny++; else nx--;

            if (IsValid(nx, ny)) {
                int blk = AI::GetInstance()->GetBlock(nx, ny);

                // TÍNH COST (CHI PHÍ)
                float cost = COST_GROUND;

                // Gặp tường mềm: Cost = 15 (Coi như mất 15s để bắn, giống JS)
                // Đây là đột phá: Nó sẽ tự cân nhắc nên đi vòng hay bắn tường
                if (blk == BLOCK_SOFT_OBSTACLE) cost = COST_SOFT_WALL;
                else if (blk != BLOCK_GROUND) continue; // Tường cứng

                // Né đồng đội (Nhưng nếu bí quá vẫn đi qua, cost cao)
                if (IsOccupied(nx, ny, myID) && !(nx == ex && ny == ey)) cost += 50.0f;

                // Né nhà mình
                if (IsMyBase(nx, ny)) cost += 200.0f;

                // Né đạn (Nguy hiểm)
                if (g_riskMap[nx][ny] > 0) cost += g_riskMap[nx][ny];

                if (u.g + cost < gScore[nx][ny]) {
                    gScore[nx][ny] = u.g + cost;

                    if (u.x == sx && u.y == sy) firstDir[nx][ny] = dir;
                    else firstDir[nx][ny] = firstDir[u.x][u.y];

                    pq.push({ nx, ny, gScore[nx][ny], GetManhattan(nx, ny, ex, ey), 0 });
                }
            }
        }
    }

    // Fallback: Random thoát kẹt nếu A* bó tay
    for (int d = 1; d <= 4; d++) {
        int nx = sx, ny = sy;
        if (d == 1) ny--; else if (d == 2) nx++; else if (d == 3) ny++; else nx--;
        if (IsValid(nx, ny) && AI::GetInstance()->GetBlock(nx, ny) == BLOCK_GROUND
            && !IsOccupied(nx, ny, myID) && !IsMyBase(nx, ny)) return d;
    }
    return 0;
}

// =========================================================================
// 4. LOGIC UPDATE & TARGETING
// =========================================================================
void PlaceTankLoop(int type) {
    AI* p = AI::GetInstance();
    int bx = 1, by = 1;
    if (!p->GetMyBases().empty()) { bx = p->GetMyBases()[0]->GetX(); by = p->GetMyBases()[0]->GetY(); }
    else { if (p->GetMyTeam() == TEAM_2) { bx = 20; by = 20; } }

    for (int r = 1; r <= 8; r++) {
        for (int dx = -r; dx <= r; dx++) for (int dy = -r; dy <= r; dy++) {
            int x = bx + dx, y = by + dy;
            if (IsValid(x, y) && p->GetBlock(x, y) == BLOCK_GROUND && !g_placed[x][y] && !IsMyBase(x, y)) {
                Game::PlaceTank(type, x, y); g_placed[x][y] = true; return;
            }
        }
    }
}

void AI_Placement() {
    for (int i = 0; i < MAP_W; i++) for (int j = 0; j < MAP_H; j++) g_placed[i][j] = false;
    PlaceTankLoop(TANK_HEAVY); PlaceTankLoop(TANK_HEAVY);
    PlaceTankLoop(TANK_MEDIUM); PlaceTankLoop(TANK_MEDIUM);
}

void AI_Update() {
    AI* p_AI = AI::GetInstance();
    BuildRiskMap();

    // Data
    vector<Tank*> enemies;
    for (int i = 0; i < 4; i++) { Tank* t = p_AI->GetEnemyTank(i); if (t && t->GetHP() > 0) enemies.push_back(t); }
    vector<PowerUp*> powerups = p_AI->GetPowerUpList();

    // Tìm Base Địch
    int enBx = -1, enBy = -1;
    for (auto b : p_AI->GetEnemyBases()) {
        if (b->GetHP() > 0) { enBx = b->GetX(); enBy = b->GetY(); if (b->GetType() == BASE_MAIN) break; }
    }
    if (enBx == -1) { enBx = (p_AI->GetMyTeam() == TEAM_1) ? 20 : 1; enBy = (p_AI->GetMyTeam() == TEAM_1) ? 20 : 1; }

    // Skill
    if (!enemies.empty()) {
        if (p_AI->HasAirstrike()) p_AI->UseAirstrike(enemies[0]->GetX(), enemies[0]->GetY());
        if (p_AI->HasEMP()) p_AI->UseEMP(enemies[0]->GetX(), enemies[0]->GetY());
    }

    for (int i = 0; i < 4; i++) {
        Tank* me = p_AI->GetMyTank(i);
        if (!me || me->GetHP() <= 0) { g_hasTarget[i] = false; continue; }

        // --- SỬ DỤNG ROUND ĐỂ LẤY TỌA ĐỘ CHÍNH XÁC ---
        int mx = Round(me->GetX());
        int my = Round(me->GetY());

        // --- 1. TARGET SCORING (Học từ JS) ---
        // Tính điểm cho từng mục tiêu để chọn cái ngon nhất
        if (!g_hasTarget[i] || (mx == g_targetX[i] && my == g_targetY[i]) || g_stuckCount[i] > 3) {
            float maxScore = -99999;
            int tx = 11, ty = 11;

            // Tank 0, 1: Ưu tiên Phá Nhà
            if (i < 2) {
                maxScore = 1000; tx = enBx; ty = enBy;
                for (auto e : enemies) { // Nếu địch quá gần thì tự vệ
                    float dist = GetDist(mx, my, e->GetX(), e->GetY());
                    if (dist < 5) {
                        float sc = 5000 - dist * 10;
                        if (sc > maxScore) { maxScore = sc; tx = Round(e->GetX()); ty = Round(e->GetY()); }
                    }
                }
            }
            // Tank 2, 3: Ưu tiên PowerUp & Giết Địch
            else {
                for (auto p : powerups) {
                    float sc = 8000 - GetDist(mx, my, p->GetX(), p->GetY()) * 10;
                    if (sc > maxScore) { maxScore = sc; tx = Round(p->GetX()); ty = Round(p->GetY()); }
                }
                for (auto e : enemies) {
                    float sc = 5000 - GetDist(mx, my, e->GetX(), e->GetY()) * 5;
                    if (e->GetHP() == 1) sc += 2000; // Last hit
                    if (sc > maxScore) { maxScore = sc; tx = Round(e->GetX()); ty = Round(e->GetY()); }
                }
                if (maxScore < 0) { tx = enBx; ty = enBy; } // Rảnh thì phá nhà
            }
            g_targetX[i] = tx; g_targetY[i] = ty; g_hasTarget[i] = true; g_stuckCount[i] = 0;
        }

        // --- 2. XỬ LÝ BẮN & DI CHUYỂN ---
        bool shoot = false;
        bool move = true;
        int moveDir = 0, shootDir = 0;

        // Check Bắn (Ưu tiên: Địch -> Nhà -> Tường)
        int fireType = 0; // 0:None, 1:Enemy/Base, 2:Wall

        for (auto e : enemies) {
            int res = RaycastCheck(i, mx, my, Round(e->GetX()), Round(e->GetY()));
            if (res == 1) {
                shoot = true; fireType = 1;
                int ex = Round(e->GetX()), ey = Round(e->GetY());
                if (ex > mx) shootDir = DIR_RIGHT; else if (ex < mx) shootDir = DIR_LEFT;
                else if (ey > my) shootDir = DIR_DOWN; else shootDir = DIR_UP;
                break;
            }
        }
        if (!shoot) {
            int res = RaycastCheck(i, mx, my, enBx, enBy);
            if (res == 1) {
                shoot = true; fireType = 1;
                if (enBx > mx) shootDir = DIR_RIGHT; else if (enBx < mx) shootDir = DIR_LEFT;
                else if (enBy > my) shootDir = DIR_DOWN; else shootDir = DIR_UP;
            }
        }

        // Tìm đường A*
        moveDir = GetMoveAStar(mx, my, g_targetX[i], g_targetY[i], i);

        // Xử lý tường mềm
        if (moveDir != 0) {
            int nx = mx, ny = my;
            if (moveDir == 1) ny--; else if (moveDir == 2) nx++; else if (moveDir == 3) ny++; else nx--;

            if (p_AI->GetBlock(nx, ny) == BLOCK_SOFT_OBSTACLE) {
                // A* bảo đi qua tường -> BẮN PHÁ
                if (!shoot || fireType == 2) {
                    shoot = true; move = false; shootDir = moveDir;
                }
            }
        }
        else {
            move = false;
        }

        // Sniper Mode (Giống JS: Đứng yên bắn cho chuẩn nếu an toàn)
        bool onBridge = ((mx >= 10 && mx <= 11) && (my >= 2 && my <= 19));
        if (shoot && fireType == 1 && g_riskMap[mx][my] == 0 && !onBridge) {
            move = false;
        }
        if (onBridge && !move && moveDir != 0) move = true; // Chống tắc cầu

        // Anti-Stuck
        if (mx == Round(g_lastX[i]) && my == Round(g_lastY[i]) && move) g_stuckCount[i]++;
        else { g_stuckCount[i] = 0; g_lastX[i] = me->GetX(); g_lastY[i] = me->GetY(); }

        if (g_stuckCount[i] > 3) {
            moveDir = rand() % 4 + 1; move = true; shoot = true; // Quẫy
            g_stuckCount[i] = 0; g_hasTarget[i] = false; // Đổi mục tiêu
        }

        int finalDir = (shoot && !move) ? shootDir : moveDir;
        if (finalDir == 0) finalDir = (shootDir != 0) ? shootDir : 1;

        Game::CommandTank(i, finalDir, move, shoot);
    }
    Game::GetInstance()->SendCommand();
}

int main(int argc, char* argv[]) {
    srand(clock());
#ifdef _WIN32
    INT rc; WSADATA wsaData; rc = WSAStartup(MAKEWORD(2, 2), &wsaData); if (rc) return 1;
#endif
    Game::CreateInstance();
    if (Game::GetInstance()->Connect(argc, argv) == -1) return -1;
    AI::GetInstance()->PlaceTank = &AI_Placement;
    AI::GetInstance()->Update = &AI_Update;
    Game::GetInstance()->PollingFromServer();
    Game::DestroyInstance();
#ifdef _WIN32
    WSACleanup();
#endif
    return 0;
}