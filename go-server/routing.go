package main

import (
	"encoding/json"
	"fmt"
	"net"
)

func route(conn net.Conn, req []byte) []byte {
	var msg RequestMessage
	err := json.Unmarshal(req, &msg)
	if err != nil {
		fmt.Println("Error unmarshalling message:", err)
		return []byte("Error unmarshalling message")
	}
	msg.conn = &conn

	resp := "Unknown command"
	switch msg.Command {
	case "open":
		resp = open(msg)
	case "create":
		resp = create(msg)
	case "join":
		resp = join(msg)
	case "ready":
		resp = ready(msg)
	case "start":
		resp = start(msg)
	}

	return []byte(resp)
}
