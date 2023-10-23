from strucutre import State
from heapq import heappush, heappop


def bfs(state: State):
    count = 0
    que = [state]
    visited = [state]
    path = []
    while que:
        current = que.pop(0)
        count += 1
        if current.is_final():
            path = current.path
            break
        neighbour = current.get_next_states()
        for i in neighbour:
            if i.not_searched(visited):
                visited.append(i)
                que.append(i)
    print(count)
    return path


def dfs(state: State):
    count = 0
    stack = [state]
    visited = [state]
    path = []
    while stack:
        current = stack.pop()
        count += 1
        if current.is_final():
            path = current.path
            break
        neighbour = current.get_next_states()
        for i in neighbour:
            if i.not_searched(visited):
                visited.append(i)
                stack.append(i)
    print(count)
    return path


def ucs(state: State):
    count = 0
    que = []
    visited = [state]
    path = []
    heappush(que, state)
    while que:
        current = heappop(que)
        count += 1
        if current.is_final():
            path = current.path
            break
        neighbour = current.get_next_states()
        for i in neighbour:
            if i.not_searched(visited):
                visited.append(i)
                heappush(que, i)
    print(count)
    return path


def heuristic(state: State):
    return state.target_y - state.y


def astar(state: State):
    count = 0
    que = []
    visited = [state]
    path = []
    heappush(que, state)
    while que:
        current = heappop(que)
        count += 1
        if current.is_final():
            path = current.path
            break
        neighbour = current.get_next_states()
        for i in neighbour:
            if i.not_searched(visited):
                i.cost += heuristic(i)
                visited.append(i)
                heappush(que, i)
    print(count)
    return path


def user_play(state: State):
    while not state.is_final():
        state.board.pr_chess()
        choices = state.get_next_states()
        c = ""
        for move in range(0, choices.__len__()):
            c += str(move)
            c += " "
        print("\n", c, "\n")
        i = int(input("enter your choice\n"))
        state = choices[i]
    state.board.pr_chess()
    print("YOU WIN")
    