package main

import (
    "database/sql"
    "fmt"
    _ "github.com/lib/pq"
    "encoding/json"
    "strings"
)

func connect(databaseInfo DatabaseInfo) Domain {
    dbinfo := fmt.Sprintf("postgres://%s:%s@%s:/%s?sslmode=disable",
        databaseInfo.DBUser, databaseInfo.DBPassword, databaseInfo.DBUrl, databaseInfo.DBName)
    db, err := sql.Open("postgres", dbinfo)
    checkErr(err)
    defer db.Close()

    fmt.Println("# Querying")
    dbQuery := fmt.Sprintf("select column_name, ordinal_position, is_nullable, udt_name from INFORMATION_SCHEMA.COLUMNS where table_name = '%s'", databaseInfo.DBTable)
    rows, err := db.Query(dbQuery)
    checkErr(err)
    domainModel := Domain{}
    domainModel.Table = databaseInfo.DBTable
    domainModel.ClassName = strings.Title(domainModel.Table)
    for rows.Next() {
        var column_name string
        var ordinal_position int
        var is_nullable string
        var udt_name string
        err = rows.Scan(&column_name, &ordinal_position, &is_nullable, &udt_name)
        checkErr(err)

        attributeName := strings.ToLower(column_name)
        if(strings.Contains(attributeName, "_")) {
            words := strings.Split(attributeName, "_")
            attributeName = words[0] + strings.Title(words[1]);
        }
        IsPrimary := false
        if(ordinal_position == 1) {
            IsPrimary = true
            domainModel.PrimaryType = getAttrType(udt_name)
        }
        domainModel.Attributes = append(domainModel.Attributes, DomainAttribute{column_name, IsPrimary, attributeName, "set" + strings.Title(attributeName), "get" + strings.Title(attributeName), getAttrType(udt_name), ordinal_position, getNullableFlag(is_nullable)})

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
    ProjectName   string    `json:"projectName"`
    Company    string `json:company`
    Table      string `json:table`
    ClassName  string `json:className`
    PrimaryType string `json:primaryType`
    Attributes []DomainAttribute    `json:"attributes"`
}

type DomainAttribute struct {
    Column        string    `json:"column"`
    Primary       bool      `json:isPrimary`
    AttributeName string    `json:"attributeName"`
    Setter string           `json:"setter"`
    Getter   string         `json:getter`
    AttributeType string    `json:"attributeType"`
    AttributePosition int   `json:"attributePos"`
    CanBeNull bool          `json:"nullable"`
}
