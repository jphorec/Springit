package main

import(
  "os"
  "fmt"
  "http/template"
  "io"
)

type Project struct {
  ProjectRoot string
  SpringType string
  Permissions os.FileMode
  Directories []string
}

func NewProject(projectRoot string, springType string) Project {
  project := Project{}
  project.ProjectRoot = projectRoot
  project.SpringType = springType
  project.Permissions = 0777
  project.Directories = GetDirectories(project.SpringType)
  return project
}

func GetDirectories(s string) []string {
  switch s {
  case "boot" :
    sArr := []string{"application", "application/common", "application/domain", "application/repository", "application/rest", "application/service"}
    return sArr
  }
  fmt.Printf("Spring type: %s, is not supported!\n", s)
  os.Exit(0)
  return []string{}
}

func CreateDirectories(project Project) {
  os.MkdirAll(project.ProjectRoot, os.FileMode(project.Permissions))
  os.Chdir(project.ProjectRoot)
  for _, directory := range project.Directories {
    os.MkdirAll(directory, project.Permissions)
  }
  CreatePomParent(project)
}

func CreatePomParent(project Project) {
  var w Writer
  pomTemplate := template.New("Parent Pom")
  t, _ = template.ParseFiles("templates/pom.xml", nil)
  t.Execute(w, project)
}
