//
// Created by Ahmed on 10/14/2018.
//
#include "ClientH.h"

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <sys/un.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>


char client[](char SentMessage[],jstring RecvMessage){

    int sockfd;
    int len;
    struct sockaddr_un address;
    int result;
    char msg[] = " ";

    strcat(msg, SentMessage);

/*  Create a socket for the client.  */

    sockfd = socket(AF_UNIX, SOCK_STREAM, 0);

/*  Name the socket, as agreed with the server.  */

    address.sun_family = AF_UNIX;
    strcpy(address.sun_path, "server_socket");
    len = sizeof(address);

/*  Now connect our socket to the server's socket.  */

    result = connect(sockfd, (struct sockaddr *)&address, len);

    if(result == -1) {
        perror("oops: client1");
        exit(1);
    }

/*  We can now read/write via sockfd.  */

    write(sockfd, &msg, 1);
    read(sockfd, &msg, 1);
    strcat(reinterpret_cast<char *>(RecvMessage), msg);
    close(sockfd);
    exit(0);


}


