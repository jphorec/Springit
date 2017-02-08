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
    os.MkdirAll(directory, project.Permissions)
    if directory != "application" {
      // os.Chdir(directory)
      subProject := Project{ProjectName: project.ProjectName, SpringType: project.SpringType, Company: project.Company, Resource: project.Resource, Backend: project.Backend, Permissions: 0755, Directories: []string{directory}}
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
func CreateJavaDir(project Project, directory string, domain Domain) {
  folder := strings.Split(directory, "/")[1]
  root := directory + "/src/main/java/com/" + project.Company + "/" + project.ProjectName + "/"
  path := root + folder
  os.MkdirAll(path, project.Permissions)
  switch folder {
    case "domain":
      domainOut, domainErr := CreateJavaFile(domain, "../templates/java/Domain.java")
      ErrorCheck(domainErr)
      WriteToFile(domainOut, path + fmt.Sprintf("/%s.java", domain.ClassName))
    case "repository":
      repoOut, repoErr := CreateJavaFile(domain, "../templates/java/Repository.java")
      ErrorCheck(repoErr)
      WriteToFile(repoOut, path + fmt.Sprintf("/%sRepository.java", domain.ClassName))
    case "rest":
      os.MkdirAll(path + "/controller", project.Permissions)
      os.MkdirAll(directory + "/src/main/resources", project.Permissions)
      os.MkdirAll(root + "config", project.Permissions)
      ctrlOut, ctrlErr := CreateJavaFile(domain, "../templates/java/Controller.java")
      ErrorCheck(ctrlErr)
      WriteToFile(ctrlOut, path + fmt.Sprintf("/controller/%sController.java", domain.ClassName))
      propOut, propErr := CreateJavaFile(domain, "../templates/java/application.properties")
      ErrorCheck(propErr)
      WriteToFile(propOut, directory + "/src/main/resources/application.properties")
      bannerOut, bannerErr := CreateJavaFile(domain, "../templates/java/banner.txt")
      ErrorCheck(bannerErr)
      WriteToFile(bannerOut, directory + "/src/main/resources/banner.txt")
      hatCfgOut, hatCfgErr := CreateJavaFile(domain, "../templates/java/HateoasConfig.java")
      ErrorCheck(hatCfgErr)
      WriteToFile(hatCfgOut, root + "/config/HateoasConfig.java")
      webCfgOut, webCfgErr := CreateJavaFile(domain, "../templates/java/WebConfig.java")
      ErrorCheck(webCfgErr)
      WriteToFile(webCfgOut, root + "/config/WebConfig.java")
      appOut, appErr := CreateJavaFile(domain, "../templates/java/Application.java")
      ErrorCheck(appErr)
      WriteToFile(appOut, root + "/Application.java")
    case "service":
      os.MkdirAll(path + "/hateoas", project.Permissions)
      hOut, hErr := CreateJavaFile(domain, "../templates/java/GenericResourceAssembler.java")
      ErrorCheck(hErr)
      WriteToFile(hOut, path + "/hateoas/GenericResourceAssembler.java")
      h2Out, h2Err := CreateJavaFile(domain, "../templates/java/GenericResourcesAssembler.java")
      ErrorCheck(h2Err)
      WriteToFile(h2Out, path + "/hateoas/GenericResourcesAssembler.java")
  }
}
