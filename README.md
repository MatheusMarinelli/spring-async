<h1>Projeto</h1>
<h4>Trabalhando com chamadas síncronas e assíncronas (spring async)</h4>

<h3>Descrição</h3>

Este projeto tem como objetivo mostrar as diferenças de performance entre chamadas síncronas e assíncronas
Para realizar as chamadas foi utilizado o webservice [SWAPI](https://swapi.dev/)

<h3>Recursos Consumidos</h3>

* [Personagens](https://swapi.dev/documentation#people)
* [Filmes](https://swapi.dev/documentation#films)

<h3>Artefatos úteis</h3>
* [Swagger](http://localhost:8080/swagger-ui/index.html)


<h3>Configurações das Threads</h3>
<p>O código abaixo define algumas configurações para as chamadas assíncronas</p>


```java
@EnableAsync // habilita o suporte para chamadas assíncronas
@Configuration
public class AsynConfig {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // define o mínimo de threads a serem utilizadas
        executor.setMaxPoolSize(10); // define o máximo de threads a serem utilizadas
        executor.setQueueCapacity(90); // define a qtde máxima de tarefas que podem ser enfileiradas
        executor.initialize();
        return executor;
    }


}
```

`setCorePollSize(int) - define a quantidade mínima de threads que podem ser usadas`

`setMaxPollSize(int) - define a quantidade máxima de threads que podem ser usadas`

`setQueueCapacity(int) - define a qunatidade máxima de elementos que podem ser enfileirados`

<b>OBS:</b> Caso a quantidade máxima de elementos a serem enfileirados exceda a configuração acima, será lançado uma `RejectedExecutionException.class`

<h3>Massa de Teste</h3>

A massa de teste abaixo foi escolhido pois dentro existem 92 recursos para serem consumidos, dessa forma é fácil perceber a diferença entre os dois tipos de chamadas

Para os testes <b>síncronos</b> foi utilizado o curl abaixo: 

`curl --location 'localhost:8080/film/6' \
--header 'request-type: sync'`


Para os testes <b>assíncronos</b> foi utilizado o curl abaixo:

`curl --location 'localhost:8080/film/6' \
--header 'request-type: async'`





