class Board:
    def __init__(self, arr):
        self.arr = arr
        
    def pr_chess(self):
        q = '\n'.join([''.join(['{:4}'.format(item) for item in row]) for row in self.arr])        
        print("______________________________________________________________")
        print(q)
        print("______________________________________________________________")


class State:
    def __init__(self, board, path, x, y, target_x, target_y, cost=1):
        self.board = board
        self.path = path
        self.x = x
        self.y = y
        self.cost = cost
        self.target_x = target_x
        self.target_y = target_y

    def is_final(self):
        return self.x == self.target_x and self.y == self.target_y

    def movement(self, x, y):  
        new_board = Board(self.board.arr.copy())
        new_board.arr[self.y][self.x] = " "
        new_board.arr[y][x] = "R"
        return new_board

    def downest(self, state):
        while state.board.arr[state.y + 1][state.x] == " " or state.board.arr[state.y + 1][state.x] == "K":
            state.board.arr[state.y + 1][state.x] = "R"
            state.board.arr[state.y][state.x] = " "
            state.y = state.y + 1
            
    def get_next_states(self):
        arr = []  
        for cell in range(0, self.board.arr[0].__len__()):
            if cell == self.x: continue
            s = State(self.movement(cell, self.y), self.path.copy(), cell, self.y, self.target_x, self.target_y, 1)
            self.downest(s)
            s.path.append(s)
            arr.append(s)
        return arr    
        
    def not_searched(self, visited):
        for s in visited:
            if self == s: return False
        return True

    def __ge__(self, other):
        return self.cost > other.cost

    def __lt__(self, other):
        return self.cost < other.cost

    def __eq__(self, other):
        return self.cost == other.cost and self.x == other.x and self.y == other.y


def pp(path):
    for state in path: 
        state.board.pr_chess()

