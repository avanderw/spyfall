package main

import (
	"fmt"
	"net"

	"github.com/gobwas/ws"
	"github.com/gobwas/ws/wsutil"
)

func notify(game Game, myConn net.Conn, m []byte) {
	for _, conn := range game.conns {
		if (*conn).RemoteAddr().String() != myConn.RemoteAddr().String() {
			err := wsutil.WriteServerMessage(*conn, ws.OpText, m)
			if err != nil {
				fmt.Printf("Error writing message to %s: %s", (*conn).RemoteAddr().String(), err.Error())
			}
		}
	}
}

func notifyAll(game Game, m []byte) {
	for _, conn := range game.conns {
		err := wsutil.WriteServerMessage(*conn, ws.OpText, m)
		if err != nil {
			fmt.Printf("Error writing message to %s: %s", (*conn).RemoteAddr().String(), err.Error())
		}
	}
}

func send(conn net.Conn, m []byte) {
	err := wsutil.WriteServerMessage(conn, ws.OpText, m)
	if err != nil {
		fmt.Printf("Error writing message to %s: %s", conn.RemoteAddr().String(), err.Error())
	}
}
