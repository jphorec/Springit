package main

import(
  "os"
  "fmt"
  "html/template"
  "bytes"
)

type Project struct {
  ProjectName string
  SpringType string
  Company string
  Resource string
  Backend string
  Permissions os.FileMode
  Directories []string
}

func NewProject(projectName string, springType string, company string, resource string, backend string) Project {
  project := Project{}
  project.ProjectName = projectName
  project.SpringType = springType
  project.Company = company
  project.Resource = resource
  project.Backend = backend
  project.Permissions = 0755 // default to open permissions
  project.Directories = GetDirectories(project.SpringType)
  return project
}

func ErrorCheck(error error) {
  if error != nil {
    fmt.Println(error)
    os.Exit(0)
  }
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

func CreateDirectories(project Project, domain Domain) {
  os.MkdirAll(project.ProjectName, os.FileMode(project.Permissions))
  os.Chdir(project.ProjectName)
  // Create the pom file on each
  out, err := CreatePomXml(project, "../templates/poms/pom.xml")
  ErrorCheck(err)
  f, err := os.Create("pom.xml")
  ErrorCheck(err)
  defer f.Close()

  f.Write(out)

  for _, directory := range project.Directories {
    os.MkdirAll(directory, project.Permissions)
    if directory != "application" {
      // os.Chdir(directory)
      subProject := Project{ProjectName: project.ProjectName, SpringType: project.SpringType, Company: project.Company, Resource: project.Resource, Backend: project.Backend, Permissions: 0755, Directories: []string{directory}}
      pomOut, pomErr := CreatePomXml(subProject, "../templates/poms/child_pom.xml")
      ErrorCheck(pomErr)
      WriteToFile(pomOut, directory + "/pom.xml")
      javaOut, javaErr := CreateDomainJavaFile(domain, "../templates/java/Domain.java")
      ErrorCheck(javaErr)
      WriteToFile(javaOut, directory + "/Domain.java")
      CreateJavaDir(subProject, directory)
    //  os.Chdir("../..")
    }
  }
}
func CreateDomainJavaFile(domain Domain, file string)(out []byte, error error){
  var buf bytes.Buffer
  t, err := template.ParseFiles(file)
  if err != nil {
    return nil, err
  }
  err = t.Execute(&buf, domain.Attributes)
  if err != nil {
    return nil, err
  }
  return buf.Bytes(), nil
}

func CreatePomXml(project Project, file string) (out []byte, error error){
  //pomFile, _ := os.Create("pom.xml")
  //pomTemplate := template.New("Parent Pom")
  // var wr io.Writer
  var buf bytes.Buffer
  t, err := template.ParseFiles(file)
  if err != nil {
    return nil, err
  }
  err = t.Execute(&buf, project)
  if err != nil {
    return nil, err
  }
  return buf.Bytes(), nil
}

func WriteToFile(input []byte, file string) {
  f, err := os.Create(file)
  ErrorCheck(err)
  defer f.Close()

  f.Write(input)
}
func CreateJavaDir(project Project, directory string) {
  os.MkdirAll(directory + "/src/main/java/com/" + project.Company + "/" + project.ProjectName + "/" + project.Resource, project.Permissions)
}
