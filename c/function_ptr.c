#include <stdio.h>

int bar(int, char*);

// 指针函数
char *echo_hello(char *s) {
    char *result;
    int len = strlen(s) + strlen("hello ") + 1;
    result = (char *)malloc(len * sizeof(char));
    memset(result,0, len * sizeof(char));
    snprintf(result, len, "%s%s", "hello ", s);
    return result;
}

int main(int args, char **argv) {
    // 函数指针
    int  (*foo) (int, char *);
    
    foo = bar;
    foo = &bar;

    foo(2, "hello world");
    (*foo)(2, "hello world");

    printf("%s\n", echo_hello("programmer!"));
}

int bar(int a, char *s) {
    printf("calling bar, int=%d, string=%s\n", a, s);
}

