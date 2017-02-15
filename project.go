package main

import(
  "os"
  "fmt"
  "text/template"
  "bytes"
  "strings"
)

type Project struct {
  ProjectName string
  SpringType string
  Company string
  Resource string
  Backend string
  Permissions os.FileMode
  Directories []string
  CurrentDirectory string
  Dependencies []ProjectDependency
}

type ProjectDependency struct {
  ProjectName string
  Company string
  Module string
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
  domain.ProjectName = project.ProjectName
  domain.Company = project.Company
  // Create the pom file on each
  out, err := CreatePomXml(project, "../templates/poms/pom.xml")
  ErrorCheck(err)
  f, err := os.Create("pom.xml")
  ErrorCheck(err)
  defer f.Close()

  f.Write(out)

  for _, directory := range project.Directories {
      if directory != "application" {
        project.Dependencies = append(project.Dependencies, ProjectDependency{ProjectName: project.ProjectName, Company: project.Company, Module: strings.Split(directory, "/")[1]})
      }
  }

  for _, directory := range project.Directories {
    os.MkdirAll(directory, project.Permissions)

    if directory != "application" {
      // os.Chdir(directory)

      subProject := Project{ProjectName: project.ProjectName, SpringType: project.SpringType, Company: project.Company, Resource: project.Resource, Backend: project.Backend, Permissions: 0755, Directories: []string{directory}, CurrentDirectory: strings.Split(directory, "/")[1], Dependencies: project.Dependencies}
      pomOut, pomErr := CreatePomXml(subProject, "../templates/poms/child_pom.xml")
      ErrorCheck(pomErr)
      WriteToFile(pomOut, directory + "/pom.xml")
      CreateJavaDir(subProject, directory, domain)
    //  os.Chdir("../..")
    }
  }
}
func CreateJavaFile(domain Domain, file string)(out []byte, error error){
  var buf bytes.Buffer
  t, err := template.ParseFiles(file)
  if err != nil {
    return nil, err
  }
  err = t.Execute(&buf, domain)
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

func GenerateModule(domain Domain, filePath string, template string) {
  out, err := CreateJavaFile(domain, template)
  ErrorCheck(err)
  WriteToFile(out, filePath)
}
func GenerateModulePom(project Project, module string, filePath string) {
  pomOut, pomErr := CreatePomXml(project, fmt.Sprintf("../templates/poms/%s_pom.xml", module))
  ErrorCheck(pomErr)
  WriteToFile(pomOut, filePath)
}
func CreateJavaDir(project Project, directory string, domain Domain) {
    folder := strings.Split(directory, "/")[1]
    root := directory + "/src/main/java/com/" + project.Company + "/" + project.ProjectName + "/"
    path := root + folder
    os.MkdirAll(path, project.Permissions)
    GenerateModulePom(project, folder, directory + "/pom.xml")
    switch folder {
      case "domain":
        GenerateModule(domain, path + fmt.Sprintf("/%s.java", domain.ClassName), "../templates/java/Domain.java")
      case "repository":
        GenerateModule(domain, path + fmt.Sprintf("/%sRepository.java", domain.ClassName), "../templates/java/Repository.java")
      case "rest":
        os.MkdirAll(path + "/controller", project.Permissions)
        os.MkdirAll(directory + "/src/main/resources", project.Permissions)
        os.MkdirAll(directory + "/src/main/asciidoc", project.Permissions)
        os.MkdirAll(root + "config", project.Permissions)
        GenerateModule(domain,  path + fmt.Sprintf("/controller/%sController.java", domain.ClassName), "../templates/java/Controller.java")
        GenerateModule(domain,  directory + "/src/main/resources/application.properties", "../templates/java/application.properties")
        GenerateModule(domain, directory + "/src/main/resources/banner.txt", "../templates/java/banner.txt")
        GenerateModule(domain, root + "/config/HateoasConfig.java", "../templates/java/HateoasConfig.java")
        GenerateModule(domain, root + "/config/WebConfig.java", "../templates/java/WebConfig.java")
        GenerateModule(domain, root + "/config/DatabaseConfig.java", "../templates/java/DatabaseConfig.java")
        GenerateModule(domain, root + "/Application.java", "../templates/java/Application.java")
        GenerateModule(domain, directory + "/src/main/asciidoc/api-guide.asciidoc", "../templates/java/api-guide.asciidoc")
      case "service":
        os.MkdirAll(path + "/hateoas", project.Permissions)
        GenerateModule(domain, path + "/hateoas/GenericResourceAssembler.java", "../templates/java/GenericResourceAssembler.java")
        GenerateModule(domain, path + "/hateoas/GenericResourcesAssembler.java", "../templates/java/GenericResourcesAssembler.java")
    }
}
