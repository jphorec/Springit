package main

import (
  "fmt"
  "encoding/json"
)

type Config struct {
  Projectname string `json:projectname`
  Company string `json:company`
  SpringType string `json:springtype`
  Backend string `json:backend`
}

func ConfigValues(configContents []byte) Config {
  var config Config
  err := json.Unmarshal(configContents, &config)
  if err != nil {
    fmt.Println("error:", err)
  }
  return config
  // fmt.Printf("%+v", config)
}
