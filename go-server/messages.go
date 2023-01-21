package main

import "net"

type RequestMessage struct {
	Command  string `json:"command"`
	GameID   string `json:"gameID"`
	PlayerID string `json:"playerID"`
	Ready    bool   `json:"ready"`
	conn     *net.Conn
}

type ResponseMessage struct {
	Request  RequestMessage `json:"request"`
	Game     Game           `json:"game"`
	Role     string         `json:"role"`
	Location string         `json:"location"`
}
