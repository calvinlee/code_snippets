#include <stdio.h>

int main(int args, char **argv) {
    char sa[] = "hello world";
    char *sp = "hello world";

    // sa++; 错误，sa是常量
    sp++;

    sa[1] = 'M';
    *(sa+2) = 'X';
    // sp[1] = 'M'; // 错误，不能修改常量值

    printf("%s\n", sa);
}
