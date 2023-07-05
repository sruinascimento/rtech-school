# Passos para solucionar o Desafio Pessoa Desenvolvedora Back-end - Projeto School


## Solucionando erros para deixar a aplicação online em minha máquina
Após baixar o projeto e subir no Github, fui executar em minha IDE `IntellijIdea` o projeto e não funciounou.

Fui olhar o log para ver qual era o erro, e na pilha de ratreio e vi
a `SchemaManagementException: missing table[tb_user]`.

Anotação `@Table(name = "tb_user")` e com base na migration mudei para `@Table`, pois assim irá pegar o nome da classe 
minúscula, porém o h2 é case insensitive por padrão.
o arquivo `data.sql` tinha inserts para a `tb_user` mudei para `User` e ajustei o sql que estava errado

`insert into tb_user (username, email, role) values ('alex', 'alex@email.com', 'INSTRUCTOR')`

`insert into User (username, email, role) values ('alex', 'alex@email.com', 'INSTRUCTOR')`

`insert into tb_user (username, email) values ('ana', 'ana@email.com', 'STUDENT')`

`insert into User (username, email, role) values ('ana', 'ana@email.com', 'STUDENT')`

Após essas alterações, a aplicação subiu sem erros.

## Explorando a aplicação iniciamente

Executei os testes de unidade, testei os endpoints com o `insomnia`.


## 1º desafio do projeto: Ajeitar a aplicação para que todos testes passem

De 16 testes, somente 1 falhava.
Era o teste que deveria retornar todos os cursos.

Analisei o teste, e fui analisar o endpoint, pois acessando o endpoint anteriormente nada era retornado.

No endpoint não tinha implementação para buscar os dados no `h2`. Então fiz uso do repository para buscar os dados e
retorna-los.
```java
   List<CourseResponse> courseResponses = courseRepository.findAll().stream()
        .map(CourseResponse::new)
        .collect(Collectors.toList());
        return ResponseEntity.ok(courseResponses);
```

Como resultado, o teste que falhava passou!

## 2º desafio do projeto: implementar aulas no curso e videos nos cursos, assim como suas validações e migrations.

### Implementar aulas no curso


```java
@PostMapping("/courses/{code}/sections")
    ResponseEntity<?> newLesson(@PathVariable("code") String code,
                                @RequestBody @Valid NewLessonRequest newSectionRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessageValidation(bindingResult));
        }

        Optional<Course> optionalCourse = courseRepository.findByCode(code);

        if (optionalCourse.isEmpty()) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        }

        Optional<User> optionalAuthor = userRepository.findByUsername(newSectionRequest.getAuthorUsername());

        if (optionalAuthor.isEmpty()) {
            return ResponseEntity.badRequest().body("Autor não encontrado");
        }

        if (optionalAuthor.get().isntInstructor()) {
            return ResponseEntity.badRequest().body("Autor não é instrutor");
        }

        Lesson section = newSectionRequest.toEntity();
        section.setCourse(optionalCourse.get());
        section.setAuthor(optionalAuthor.get());
        sectionRepository.save(section);

        return ResponseEntity.ok(new LessonResponse(section));
    }
```
Criei o método `newLesson` e implementei todas validações.



