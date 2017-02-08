package main

import (
  "fmt"
  "encoding/json"
)

type Config struct {
  Projectname string `json:projectname`
  Company string `json:company`
  Resource string `json:resource`
  SpringType string `json:springtype`
  Backend string `json:backend`
  DatabaseInfo DatabaseInfo `json:"dbInfo"`
}

type DatabaseInfo struct {
    DBUser string `json:dbUser`
    DBPassword string `json:dbPassword`
    DBName string `json:dbName`
    DBTable string `json:dbTable`
    DBUrl string `json:dbUrl`
    Projectname string
}

func ConfigValues(configContents []byte) Config {
  var config Config
  err := json.Unmarshal(configContents, &config)
  if err != nil {
    fmt.Println("error:", err)
  }
  fmt.Printf("%+v", config)

  return config
}
