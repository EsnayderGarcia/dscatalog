# Bem vindo(a) ao dscatalog

Neste aplicação está sendo desenvolvida uma API Rest que fornece recursos de um CRUD de categorias, produtos e usuários de um catálogo de produtos online. Esta é um dos
projetos práticos desenvolvidos no bootcamp spring 3.0 ministrado pelo Professor Nélio Alves.

## Tecnologias utilizadas
- Java 17;
- Spring Boot 2.4;
- Spring Data JPA;
- Banco de Dados H2.

Ainda serão implementados os testes unitários e de integração utilizando a o Junit 5, e a camada de segurança com o Spring Security.

## Modelo Conceitual do dscatalog

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183255418-0b46bbfb-6368-4ea8-a1d3-74d51e9c1359.png width=850px />
</div>

## Features

### Categoria
- Inserção;
- Atualização;
- Deleção;
- Consulta paginada e por id.

### Produto
- Inserção;
- Atualização;
- Deleção;
- Consulta paginada e por id.

Abaixo alguns recuros sendo acessados no Insomnia:

- Inserção de categoria e produto na base de dados

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183256249-a50d9149-4c6f-4df2-aa23-8cda5a636371.png width=850px />
</div>

</br>

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183256250-16a2cd3f-4516-416e-95a2-ea3b3c0e0298.png width=850px />
</div>

</br>
</br>

- Busca por id de categoria e produto

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183256371-8ec42a95-f9d3-46d5-b004-212a5cc880ae.png width=850px />
</div>

</br>

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183256374-7722abd9-84c3-4091-809d-b02e83570083.png width=850px />
</div>

</br>
</br>

- Tratamento de Exceção ao tentar deletar um produto ou categoria ausente na base de dados

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183256479-c11801f6-0382-4c09-b75c-7a60f2b96a4b.png width=850px />
</div>

</br>

<div align=center>   
 <img src=https://user-images.githubusercontent.com/108491940/183256480-0c723aaf-c616-4a27-a0a4-06a21b87f272.png width=850px />
</div>

