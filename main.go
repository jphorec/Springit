package main

import "fmt"
import "os"
import "io/ioutil"

func main()  {
  configPath := os.Args[1]

  buffer,err := ioutil.ReadFile(configPath)
  if err != nil {
    fmt.Print(err)
  }
  config := ConfigValues(buffer)
  domain := connect();
  fmt.Printf("Creating project directory %s...\n", config.Projectname)
  project := NewProject(config.Projectname, config.SpringType, config.Company, config.Resource, config.Backend)
  CreateDirectories(project, domain)
  fmt.Println(project)
//  os.MkdirAll(config.Projectname, 0777)
}
