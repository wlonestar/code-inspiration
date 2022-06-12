//
// Created by wjl15 on 2022/6/11.
//

// https://gist.github.com/judofyr/18cc1e9e4f48a13483c00d1c86e34cf5

#include <string>
#include <iostream>

#define REFLECT(x)  \
  template<class T> \
  void reflect(T &t) { t x; }

struct user {
  std::string name;
  int age;
  REFLECT(("name", name)("age", age))
};

class JSONWriter {
private:
  std::ostream &out;
  bool needs{};

public:
  explicit JSONWriter(std::ostream &out) : out(out) {}

  template<class T>
  auto write(T &obj) -> decltype(obj.reflect(*this), void()) {
    out << "{";
    needs = false;
    obj.reflect(*this);
    out << "}";
  }

  void write(int value) {
    out << value;
  }

  void write(std::string &value) {
    out << '"' << value << '"';
  }

  template<class T>
  JSONWriter &operator()(const char *name, T &field) {
    if (needs) {
      out << ",";
    }
    needs = true;
    out << name << ":";
    write(field);
    return *this;
  }

};

int main() {
  JSONWriter json(std::cout);
  user u = { "wjl", 12 };
  json.write(u);
  return 0;
}
