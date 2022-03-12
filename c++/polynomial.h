//
// Created by wjl on 2022/2/26.
//

#ifndef POLYNOMIAL_H
#define POLYNOMIAL_H

#pragma once

#include <iostream>
using namespace std;
#include <cmath>

namespace dst {

    struct pdata {
        double coef; double expo;
        pdata() = default;
        pdata(double c, double e) : coef(c), expo(e) {}
    };

    struct pnode {
        pdata data; pnode *prev; pnode *succ;
        pnode() = default;
        pnode(double c, double e, pnode *p = nullptr, pnode *s = nullptr) : data(pdata(c, e)), prev(p), succ(s) {}
        pnode *insert_prev(const double &c, const double &e);
        pnode *insert_succ(const double &c, const double &e);
    };

    pnode *pnode::insert_prev(const double &c, const double &e) {
        auto *x = new pnode(c, e, prev, this);
        prev->succ = x; prev = x;
        return x;
    }

    pnode *pnode::insert_succ(const double &c, const double &e) {
        auto *x = new pnode(c, e, this, succ);
        succ->prev = x; succ = x;
        return x;
    }

    class polynomial {
    private:
        int _size; pnode *header; pnode *trailer;
        void init();
        int clear();
        void copynodes(pnode *p, int n);
    public:
        polynomial();
        polynomial(const polynomial &p);
        polynomial(const polynomial &p, int r, int n);
        polynomial(pnode *p, int n);
        ~polynomial();
        int size() const;
        bool empty() const;
        polynomial &operator=(const polynomial &p);
        pnode *first() const;
        pnode *last() const;
        pnode *insertfirst(const double &coef, const double &expo);
        pnode *insertlast(const double  &coef, const double &expo);
        pnode *insert(const double &coef, const double &expo, pnode *p);
        pnode *insert(pnode *p, const double &coef, const double &expo);
        void remove(pnode *p);
        void reverse();
        void traverse() const;
        void add(polynomial &p);
        double evaluate(double x) const;
    };

    void polynomial::init() {
        header = new pnode; trailer = new pnode;
        header->succ = trailer; header->prev = nullptr;
        trailer->prev = header; trailer->succ = nullptr;
        _size = 0;
    }

    int polynomial::clear() {
        int old = _size;
        while (0 < _size) { remove(header->succ); }
        return old;
    }

    void polynomial::copynodes(pnode *p, int n) {
        init();
        while (n--) { insertlast(p->data.coef, p->data.expo); p = p->succ; }
    }

    polynomial::polynomial() {
        init();
    }

    polynomial::polynomial(const polynomial &p) {
        copynodes(p.first(), p._size);
    }

    polynomial::polynomial(const polynomial &p, int r, int n) {
        pnode *pn= p.first();
        while (0 < r--) { pn = pn->succ; }
        copynodes(pn, n);
    }

    polynomial::polynomial(pnode *p, int n) {
        copynodes(p, n);
    }

    polynomial::~polynomial() {
        clear(); delete header; delete trailer;
    }

    int polynomial::size() const {
        return _size;
    }

    bool polynomial::empty() const {
        return _size <= 0;
    }

    polynomial &polynomial::operator=(const polynomial &p) {
        copynodes(p.first(), p.size());
        return *this;
    }

    pnode *polynomial::first() const {
        return header->succ;
    }

    pnode *polynomial::last() const {
        return trailer->prev;
    }

    pnode *polynomial::insertfirst(const double &coef, const double &expo) {
        _size++; return header->insert_succ(coef, expo);
    }

    pnode *polynomial::insertlast(const double &coef, const double &expo) {
        _size++; return trailer->insert_prev(coef, expo);
    }

    pnode *polynomial::insert(const double &coef, const double &expo, pnode *p) {
        _size++; return p->insert_prev(coef, expo);
    }

    pnode *polynomial::insert(pnode *p, const double &coef, const double &expo) {
        _size++; return p->insert_succ(coef, expo);
    }

    void polynomial::remove(pnode *p) {
        p->prev->succ = p->succ; p->succ->prev = p->prev;
        delete p; _size--;
    }

    void polynomial::reverse() {
        pnode *p = header; pnode *q = trailer;
        for (int i = 1; i < _size; i+=2) {
            swap((p = p->succ)->data, (q = q->prev)->data);
        }
    }

    void polynomial::traverse() const {
        pnode *p = first(); cout << "polynomial=";
        for (int i = 0; i < _size - 1; i++) {
            cout << p->data.coef << "*x^" << p->data.expo << "+"; p = p->succ;
        }
        cout << last()->data.coef << "*x^" << last()->data.expo << "\n";
    }

    void polynomial::add(polynomial &p) {
        pnode *pa = this->first(); pnode *pb = p.first();
        polynomial lc; pnode *pc = lc.header;
        while (pa != this->trailer && pb != p.trailer) {
            if (pa->data.expo == pb->data.expo) {
                lc.insert(pc, pa->data.coef + pb->data.coef, pa->data.expo);
                pa = pa->succ; pb = pb->succ; pc = pc->succ; this->remove(pa->prev);
            } else if (pa->data.expo < pb->data.expo) {
                lc.insert(pc, pa->data.coef, pa->data.expo);
                pa = pa->succ; pc = pc->succ; this->remove(pa->prev);
            } else if (pa->data.expo > pb->data.expo) {
                lc.insert(pc, pb->data.coef, pb->data.expo);
                pa = pa->succ; pc = pc->succ; p.remove(pb->prev);
            }
        }
        if (pa == this->trailer) {
            while (pb != p.trailer) {
                lc.insert(pc, pb->data.coef, pb->data.expo);
                pb = pb->succ; pc = pc->succ; p.remove(pb->prev);
            }
        } else if (pb == p.trailer) {
            while (pa != this->trailer) {
                lc.insert(pc, pa->data.coef, pa->data.expo);
                pa = pa->succ; pc = pc->succ; this->remove(pa->prev);
            }
        }
        *this = lc; lc.clear();
    }

    double polynomial::evaluate(double x) const {
        pnode *p = first();
        double result = 0.0;
        for (int i = 0; i < _size; i++) {
            result += (p->data.coef * pow(x, p->data.expo)); p = p->succ;
        }
        return result;
    }

}

#endif //POLYNOMIAL_H
