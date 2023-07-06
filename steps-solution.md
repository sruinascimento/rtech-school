# Passos para solucionar o Desafio Pessoa Desenvolvedora Back-end - Projeto School

Nesse arquivo, serão escritas de maneira suscinta os passos que fiz para chegar na solução do desafio.

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

- A implementação do método `findAll()` da interface `CourseRepository` foi realizada no `CourseController`, dentro do
  método`allCourses()`.

Após realizar a implementação, o problema foi solucionado e o teste falhou.

## 2º desafio do projeto: implementar aulas no curso e videos nos cursos, assim como suas validações e migrations.

### Aulas no curso

Foram criados:

- A migration para a tabela `Section`. A modelagem é que várias `Section` estão associadas a um `Course`.
  uma `Section` contém um autor(`User`).
- A entidade `Section` mapeada, atributos `User author` e `Course course` com a anotação `@ManyToOne`
- A classe `NewSectionRequest` que é o `DTO` para a requisição para criar novas aulas.
- A interface `SectionRepository` com o método `Optional<Section> findByCode(String code);`
- A classe `NewSectionResponse`, `DTO` para responder após à criação do aula.
- a classe `SectionController` para cadastrar `Section`. 

**obs:** Iniciamente criei as classes com o prefixo `Lesson` ou inclusão da mesma, após dúvidas sobre a section, o meu
mentor @aquiles, esclareceu essa idea de section que é aula. Tive que refatorar, e ao fazer esta refatoração, tive que fazer uso dos
testes unitários, por mais que seja simples, pode "quebrar" a aplicação, e quebrou quando só mudei todas as ocorrências
de Lesson para Section.

### Videos nas aulas

Foram criados:

- A migration para a tabela `Video`. A modelagem é que um `Section` contém um ou mais `Video`.
  Um `Video` não pode se repetir na `Section`, como consequência, inferi que o vídeo não deve se repetir em outros
  cursos, logo o atributo `url` link do vídeo com a restrição `unique`.
- A entidade `Video` mapeada, atributos `Section section` com a anotação `@ManyToOne`
- A classe `NewVideoRequest` que é o `DTO` para a requisição para conter os dados dos novos videos.
- A interface `VideoRepository`.
- A classe `NewVideoResponse`, `DTO` para responder após à criação do video.
- A classe `VideoController` para cadastrar video novo.

Classe alterada:

- A classe `Section` foi alterada para ter uma lista de `Video` com a anotação `@OneToMany`

## 3º desafio do projeto: Implementar a matrícula de usuário

Foram criados:

- A migration para a tabela `Enrollment`. Um curso pode ter vários alunos matriculados, porém uma matrícula não pode se 
repetir no mesmo curso. Logo foi colocada a restrição de `unique` User e Course.
- A entidade `Enrollment` mapeada, atributos `Course course` e `User` com a anotação `@ManyToOne`
- A classe `NewEnrollmentRequest` que é o `DTO` para a requisição para conter os dados das novas matriculas.
- A interface `EnrollmentRepository` com o método 
  `Optional<Enrollment> findByUser_UsernameAndCourse_Code(String username, String courseCode);`. Esse método serve para
  pesquisar o usuário e o curso que o mesmo está cadastrado, serve para validar e não cadastrar usuário repetido.
- A classe `NewEnrollmentResponse`, `DTO` para responder após à criação da matrícula.
- A classe `EnrollmentController` para cadastrar nova matrícula.

### Testes de unidade: `EnrollmentTest`
- Test para adicionar matrícula
- Test para invalidar `userName` em branco
- Test para inavalidar matricula repetida
- Test para invalidar requisição ruim com curso inválido


## 4º desafio do projeto: Implementar relatório de vídeos por aula

Foram criados:

- A interface `ReportRepository` com o método
  `List<ReportProjection> findSectionByVideosReport();`. 
  com a query natina:
```sql
SELECT c.name AS courseName, s.title AS sectionTitle, u.username AS authorName, COUNT(v.id) AS totalVideos
FROM Course c
INNER JOIN Section s ON c.id = s.course_id
INNER JOIN User u ON s.author_id = u.id
LEFT JOIN Video v ON s.id = v.section_id
INNER JOIN Enrollment e ON c.id = e.course_id
GROUP BY c.name, s.title, u.username;
```
- A interface `ReportProjection`,projeção para recuperar somente os atributos resultante da query.
- A classe `ReportController` para receber a requisição do relatório retornar o relatório.
- A classe `ReportResponse` que é o `DTO`.