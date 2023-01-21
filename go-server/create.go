package main

import (
	"encoding/json"
	"math/rand"

	"github.com/patrickmn/go-cache"
)

func create(msg RequestMessage) string {
	m, err := json.Marshal(ResponseMessage{Request: msg, Game: createGame()})
	if err != nil {
		return "Error creating game"
	}

	return string(m)
}

func createGame() Game {
	id := generateID()
	game := Game{ID: id}
	gameCache.Set(id, game, cache.DefaultExpiration)
	return game
}

func generateID() string {
	id := ""
	for i := 0; i < 5; i++ {
		id += string(rune(65 + rand.Intn(26)))
	}
	return id
}

func createGameWithId(id string) Game {
	game := Game{ID: id}
	return game
}
