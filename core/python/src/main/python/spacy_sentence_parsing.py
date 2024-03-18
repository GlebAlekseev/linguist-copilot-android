import json
import spacy

nlp = spacy.load("en_core_web_sm")

class Node:
    def __init__(self, token, pos, rel, token_x_start, token_x_end, parent=None):
        self.token = token   # строка, представляющая токен
        self.pos = pos       # строка, представляющая часть речи
        self.rel = rel       # строка, представляющая отношение
        self.token_x_start = token_x_start
        self.token_x_end = token_x_end
        self.group_x_start = token_x_start  # начало группы узлов по умолчанию равно началу текущего узла
        self.group_x_end = token_x_end      # конец группы узлов по умолчанию равен концу текущего узла
        self.parent = parent # ссылка на родительский узел
        self.children = []   # список дочерних узлов

    def add_child(self, child_node):
        self.children.append(child_node)
        child_node.parent = self
        # Обновляем начало и конец группы узлов текущей ноды
        if child_node.token_x_start < self.group_x_start:
            self.group_x_start = child_node.token_x_start
        if child_node.token_x_end > self.group_x_end:
            self.group_x_end = child_node.token_x_end
        # Обновляем начало и конец группы узлов для всех родительских узлов до корня
        parent = self.parent
        while parent is not None:
            if child_node.token_x_start < parent.group_x_start:
                parent.group_x_start = child_node.token_x_start
            if child_node.token_x_end > parent.group_x_end:
                parent.group_x_end = child_node.token_x_end
            parent = parent.parent


    def print_tree(self, indent=0):
        print("  " * indent + f"[Node] Token: {self.token} | POS: {self.pos} | REL: {self.rel} | TXS: {self.token_x_start} | TXE: {self.token_x_end} | GXS: {self.group_x_start} | GXE: {self.group_x_end}")
        for child in self.children:
            child.print_tree(indent + 1)

    def to_dict(self):
        children_dict = [child.to_dict() for child in self.children]
        return {
            "token": self.token,
            "pos": self.pos,
            "rel": self.rel,
            "token_x_start": self.token_x_start,
            "token_x_end": self.token_x_end,
            "group_x_start": self.group_x_start,
            "group_x_end": self.group_x_end,
            "children": children_dict
        }
    def __str__(self):
        return f"[Node] Token: {self.token} | POS: {self.pos} | REL: {self.rel} | TXS: {self.token_x_start} | TXE: {self.token_x_end} | GXS: {self.group_x_start} | GXE: {self.group_x_end}"


def build_tree(root_token):
    root = Node(root_token.text, root_token.pos_, root_token.dep_, root_token.idx, root_token.idx + len(root_token.text))
    stack = [root_token]
    visited = set()

    while stack:
        current_token = stack.pop()
        visited.add(current_token)

        current_node = find_node(root, current_token.text)
        for child_token in current_token.children:
            if child_token not in visited:
                child_node = Node(child_token.text, child_token.pos_, child_token.dep_, child_token.idx, child_token.idx + len(child_token.text))
                current_node.add_child(child_node)
                stack.append(child_token)

    return root

def find_node(node, token_text):
    if node.token == token_text:
        return node
    for child in node.children:
        found_node = find_node(child, token_text)
        if found_node:
            return found_node
    return None

def process(sentence):
    doc = nlp(sentence)
    root_token = None
    for token in doc:
        if token.head == token:  # Проверяем, ссылается ли голова токена на самого себя
            root_token = token
            break
    if root_token is not None:
        tree = build_tree(root_token)
        print("Tree structure:")
        tree.print_tree()
        json_tree = tree.to_dict()
        print("JSON representation:")
        return json.dumps(json_tree, indent=2)
    else:
        print("Root token not found in the document.")
        return None

# Пример использования:
if __name__ == "__main__":
    sentence = "James went to the corner shop to buy some eggs, milk and bread for breakfast."
    result = process(sentence)
    print(result)