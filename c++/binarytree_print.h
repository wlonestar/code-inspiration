//
// Created by wjl on 2022/2/28.
//

#ifndef BINARYTREE_PRINT_H
#define BINARYTREE_PRINT_H

template<typename T>
void bintree<T>::printsibling(const string &prefix, const btnode<T> *node) {
    if (node) {
        cout << prefix;
        if (hasparent(*node)) {
            cout << (islchild(*node) ? "l---" : "r---");
        } else {
            cout << "----";
        }
        cout << node->data << "\n";
        string pre = (islchild(*node) ? "|   " : "    ");
        if (haslchild(*node)) {
            printsibling(prefix + pre, node->lc);
        }
        if (hasrchild(*node)) {
            printsibling(prefix + pre, node->rc);
        }
    }
}

template<typename T>
void bintree<T>::print(btnode<T> *node) {
    printsibling("", node);
}

#endif //BINARYTREE_PRINT_H
