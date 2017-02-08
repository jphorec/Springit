package main

import (
    "database/sql"
    "fmt"
    _ "github.com/lib/pq"
    "encoding/json"
)

const (
    DB_USER     = "postgres"
    DB_PASSWORD = "password"
    DB_NAME     = "basketballapp"
    DB_URL      = "localhost"
)

func connect() Domain {
    dbinfo := fmt.Sprintf("postgres://%s:%s@%s:/%s?sslmode=disable",
        DB_USER, DB_PASSWORD, DB_URL, DB_NAME)
    db, err := sql.Open("postgres", dbinfo)
    checkErr(err)
    defer db.Close()

    fmt.Println("# Querying")
    rows, err := db.Query("select column_name, ordinal_position, is_nullable, udt_name from INFORMATION_SCHEMA.COLUMNS where table_name = 'player'")
    checkErr(err)
    domainModel := Domain{}

    for rows.Next() {
        var column_name string
        var ordinal_position int
        var is_nullable string
        var udt_name string
        err = rows.Scan(&column_name, &ordinal_position, &is_nullable, &udt_name)
        checkErr(err)
        domainModel.Attributes = append(domainModel.Attributes, DomainAttribute{column_name, getAttrType(udt_name), ordinal_position, getNullableFlag(is_nullable)})
    }
    jsonDomain,_ := json.Marshal(domainModel)
    fmt.Println(string(jsonDomain))
    return domainModel
}

func queryTable() {
}
func getNullableFlag(s string) bool {
    if(s == "YES") {
        return true
    } else {
        return false
    }
}
func getAttrType(s string) string {
    switch s {
    case "int4":
        return "Integer"
    case "varchar":
        return "String"
    default: 
        return "String"
    }
}
func checkErr(err error) {
    if err != nil {
        panic(err)
    }
}

type Domain struct {
    Attributes []DomainAttribute    `json:"attributes"`
} 

type DomainAttribute struct {
    AttributeName string    `json:"attributeName"`
    AttributeType string    `json:"attributeType"`
    AttributePosition int   `json:"attributePos"`
    CanBeNull bool          `json:"nullable"`
}

