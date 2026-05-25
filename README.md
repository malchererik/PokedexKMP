# Pokédex Multiplatform — Trabalho M2

**Disciplina:** Programação para Dispositivos Móveis II  
**Professor:** Welington Gadelha  
**Instituição:** Universidade do Vale do Itajaí (UNIVALI)  
**Alunos:** Pedro Teles e Erik Malcher

---

## 1. Descrição do Projeto
Este projeto consiste na evolução da Pokédex Multiplatform (KMP) desenvolvida na etapa M1. A camada de dados simulada foi integralmente substituída por uma arquitetura híbrida robusta baseada em **consumo de API externa em tempo real** e **persistência local definitiva**, seguindo os padrões recomendados de arquitetura e gerenciamento de ciclo de vida.

---

## 2. Arquitetura e Gerenciamento de Estado
A aplicação adota os princípios de **Clean Architecture** e o padrão **MVVM (Model-View-ViewModel)** com Fluxo de Dados Unidirecional (UDF), eliminando o acoplamento de regras de negócio na camada de visualização (UI):

* **ViewModel Oficial do Lifecycle (KMP):** Gerencia o ciclo de vida de forma reativa e mantém a integridade dos dados durante rotações de tela e mudanças de configuração.
* **Abstração por Interfaces:** O `PokedexViewModel` depende exclusivamente da interface abstrata `PokemonRepository`, garantindo inversão de dependências e facilitando testes de unidade.
* **Estado de Tela Único (StateFlow):** A interface consome um fluxo reativo que representa explicitamente os estados operacionais da aplicação:
    * `PokedexUiState.Loading`: Disparado durante a sincronização ou consultas iniciais.
    * `PokedexUiState.Success`: Fornece a lista de Pokémons acumulada e controla os limites de páginas.
    * `PokedexUiState.Error`: Captura falhas de banco ou rede, disparando layouts de recuperação com opção de re-tentativa.

---

## 3. Camada de Dados e Estratégia Offline-First
A sincronização e busca de dados implementam uma estratégia otimizada de cache local para poupar largura de banda e viabilizar o uso do app sem conectividade:

* **Sincronização Inicial (Cache Local):** Na primeira inicialização, o repositório realiza uma chamada HTTP para extrair os nomes e IDs dos primeiros 150 Pokémons, salvando-os permanentemente na tabela `pokemon_cache`. Nas aberturas subsequentes, o app valida o cache e evita acessos redundantes à rede.
* **Paginação Nativa em SQL:** A listagem no grid consome blocos dinâmicos sob demanda de 20 em 20 registros. Conforme o usuário realiza o scroll, o `LazyVerticalGrid` dispara novos carregamentos executados diretamente no SQLite via cláusulas `LIMIT` e `OFFSET`.
* **Filtros Dinâmicos no Banco:** A barra de pesquisa (SearchBar) executa buscas nativas contra o banco de dados Room utilizando o operador SQL `LIKE`, proibindo filtros em memória Kotlin.
* **Consulta HTTP em Tempo Real (Ktor):** Ao abrir a tela de Detalhes, o aplicativo faz uma requisição HTTP assíncrona direta à PokeAPI via cliente `Ktor3`, extraindo atributos dinâmicos atualizados (peso, altura, tipos e estatísticas base) e imagens de alta resolução oficial-artwork.

---

## 4. Persistência Local e Regras de Negócio Customizadas
A persistência utiliza a biblioteca oficial **Room KMP** integrada ao motor assíncrono do Kotlin (`Coroutines` e `Flow`):

* **Banco de Dados Relacional:** Estruturação das entidades locais `PokemonCacheEntity` (Pokedex) e `TeamPokemonEntity` (Mochila de Favoritos).
* **Regra Obrigatória de Captura:** Ao favoritar ou adicionar um Pokémon ao time dentro da tela de detalhes, a aplicação intercepta a ação e exibe um `AlertDialog` com input obrigatório de texto (`captureLocation`), persistindo de forma definitiva onde a criatura foi encontrada.
* **Escuta Reativa:** A tela do time utiliza `Flow` mapeado diretamente do Room, atualizando os cartões de forma automática assim que um registro é adicionado ou removido.

---

## 5. Estrutura de Navegação
O ecossistema utiliza as rotas fortemente tipadas do `Navigation Compose` de forma limpa:
* `PokedexRoute`: Tela principal com busca, paginação e grid de listagem.
* `PokemonDetailRoute(pokemonId: Int)`: Detalhes carregados em tempo real com tratamento de rolagem adaptativa (`verticalScroll`).
* `TeamRoute`: Listagem dos favoritos armazenados localmente no dispositivo.


