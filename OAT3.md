# OAT 3 - Indexando os meus dados

Equipe Rio de Janeiro. Java 17 ou superior (JDK).

## Executar

Na raiz do repositório:

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/main/java/br/com/mecaniqa/*.java
java -cp out br.com.mecaniqa.MecaniQAApplication
java -cp out br.com.mecaniqa.ValidacaoOat3
```

No Windows, crie a pasta `out` com `mkdir out` se ainda não existir. Os demais comandos são os mesmos.
O programa carrega `dados/*.csv` ao iniciar e salva na opção 0.
Pode-se informar outra pasta como primeiro argumento da aplicação.
Arquivos inexistentes representam coleções vazias. Erros de carregamento encerram o programa sem sobrescrever os arquivos.
O formato legado usa UTF-8, cabeçalho e separador `;`, sem aspas/escape; textos no menu não podem conter separador ou quebras de linha.

## Arquitetura

- `No<T>`: struct com ID, referência, altura e filhos; folha tem altura 1.
- `ArvoreAvl`: métodos estáticos para inserção, cálculo de altura/fator, quatro rotações, busca e percurso em ordem; implementados do zero.
- `Indexador<T>`: encapsula a raiz e recebe `ToIntFunction<T>` para extrair o ID. Rejeita nulos e IDs duplicados.
- `Gerenciador`: índices independentes de Cliente, Peça, Serviço e OS. Busca por código retorna o objeto completo exclusivamente pela AVL. Listagens por código percorrem a árvore sem array temporário ou ordenação.
- `ImportadorCsv`: carrega as quatro entidades e recompõe vínculos de carros, pedidos, itens e serviços das OS. A fila é recomposta para OS aguardando execução. Os contadores de OS e pedido respeitam os IDs restaurados.
- Menu: opções 1–17 exercitam cadastro, consultas, relatórios e atualização/remoção de peças e serviços.

As listas existentes continuam como armazenamento para a exportação e recursos da OAT 2, com visões públicas de leitura para as entidades indexadas. Não modificar diretamente o campo `codigo` de objetos já cadastrados. As alterações de peças/serviços preservam as referências já utilizadas pelos pedidos/OS; exclusão de registro vinculado é rejeitada. A exclusão física de peça/serviço reconstrói o índice (deleção AVL completa não é exigida). A ordenação legada por nome continua disponível; os relatórios por ID usam a AVL.

## Complexidades

| Operação | Custo |
|---|---|
| Inserção / busca AVL | O(log n) |
| Relatório em ordem | O(n) |
| Memória de cada índice | O(n) |
| Recarga / reconstrução | O(n log n) |

A medida de 14 visitas na árvore com 10.000 IDs é uma contagem de nós, não uma medição de tempo. Não se atribui custo logarítmico à leitura completa dos CSV nem às exclusões por reconstrução.

## Validação executada

`ValidacaoOat3` cobre QA-01 a QA-04 dos tutoriais, quatro rotações, nulos, duplicados, ausentes, IDs negativos, 10.000 IDs crescentes e 2.000 aleatórios com semente 71, regra BST, alturas, fator de balanceamento, quatro tipos de entidade, referências após atualização, remoção e vínculos após exportar/recarregar CSV. Resultado: **36.052 verificações aprovadas**. Altura 14 e 14 nós visitados na busca do ID 10.000.

Também foi exercitado o menu com cadastro de peças fora de ordem, listagem, busca, gravação, reinício e nova listagem.

## Apresentação

`mecaniQA_oat3_rio-de-janeiro.pdf` é a versão exigida pelo enunciado; o arquivo `.pptx` é editável e inclui roteiro nas notas para cerca de 6min25s. O modelo oficial de oito slides foi preservado e ilustrado com renderizações fiéis de trechos do código e resultados reais dos testes.

Fontes da atividade: [enunciado](https://docs.google.com/document/d/15CSPC74Byet1Xl08vCWcdX_y9sHfcCZOhGAmbmQZLIg/edit), [tutoriais](https://docs.google.com/document/d/1gRnnJirruYRt0DDCW_epawGNqWlC3Z91VjbdQEIwIrY/edit) e [modelo](https://docs.google.com/presentation/d/1HayWJi__y8W4vx_2zcrZtigDn02aBJdoKDkKZiKsjf0/edit).
