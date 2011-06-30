#include <stdio.h> 
#include <sys/vfs.h>

int main()
{
    struct statfs sfs;
    statfs("/home",&sfs);
    int percent = (sfs.f_blocks-sfs.f_bfree)*100/(sfs.f_blocks-sfs.f_bfree+sfs.f_bavail);
    printf("/home   %ld %ld %ld %d%%    /home\n",4*sfs.f_blocks,4*(sfs.f_blocks-sfs.f_bfree),4*sfs.f_bavail,percent);
    system("df /home");
    return 0;
}
