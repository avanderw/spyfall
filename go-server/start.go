package main

import (
	"encoding/json"
	"math/rand"
)

func start(req RequestMessage) string {
	game, err := findGame(req.GameID)
	if err != nil {
		return err.Error()
	}

	spy := game.Players[rand.Intn(len(game.Players))]
	location := locations[rand.Intn(len(locations))]

	for _, p := range game.Players {
		if p.ID == spy.ID {
			msg := ResponseMessage{Request: req, Game: game, Role: "Spy", Location: "???"}
			m, err := json.Marshal(msg)
			if err != nil {
				return err.Error()
			}
			send(*p.conn, m)
		} else {
			msg := ResponseMessage{Request: req, Game: game, Role: location.Roles[rand.Intn(len(location.Roles))], Location: location.Name}
			m, err := json.Marshal(msg)
			if err != nil {
				return err.Error()
			}
			send(*p.conn, m)
		}
	}

	msg := ResponseMessage{Game: game}
	m, err := json.Marshal(msg)
	if err != nil {
		return err.Error()
	}

	return string(m)
}
