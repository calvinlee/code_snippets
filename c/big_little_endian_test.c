#include <stdio.h>

int main(){
    int x = 1;
    if(*(char *)&x == 1) {
        printf("Little endian!\n");
    }else{
        printf("Big endian!\n");
    }

    printf("Value of EOF is %d\n",EOF);
}
