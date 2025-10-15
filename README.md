# Análise de Desempenho de Tabelas Hash em Java

## Descrição do Projeto

Este projeto implementa e analisa o desempenho de diferentes abordagens de tabelas hash em Java, comparando estratégias de **rehashing** e **encadeamento** com diferentes funções hash. O estudo avalia o comportamento dessas estruturas de dados frente a diferentes tamanhos de vetor e volumes de dados, seguindo rigorosamente os requisitos especificados.

## Estrutura do Projeto

```
.
├── src/                    # Código fonte Java
│   ├── Main.java          # Classe principal para execução dos testes
│   ├── Registro.java      # Classe que representa um registro com código de 9 dígitos
│   ├── Hash.java          # Classe base para implementações de hash
│   ├── HashRehashing.java # Implementação com rehashing (linear/quadrático)
│   ├── HashDuplo.java     # Implementação com hash duplo
│   ├── HashEncadeamento.java # Implementação com encadeamento
│   └── EstatisticaHash.java # Classe para coleta de estatísticas
│
├── plots/                # Scripts e resultados de análise
│   ├── plots/ 
│   │   ├── plot.py           # Script Python para geração de gráficos
│   │   ├── requirements.txt  # Dependências do Python
│   └── [tamanhos]/       # Diretórios com resultados para cada tamanho
└── out/                  # Arquivos compilados
```

---

## Implementações de Tabela Hash

## Estruturas de Dados e Classes

### Classe `EstatisticaHash`
Armazena todas as métricas coletadas durante os testes:
- **Colisões**: Número total de colisões ocorridas
- **Elementos Únicos Inseridos**: Quantidade de elementos inseridos sem duplicatas
- **Tempos**: Inserção e busca em nanossegundos
- **Buscas**: Contadores de buscas bem-sucedidas e mal-sucedidas
- **Gaps**: Estatísticas sobre espaços vazios na tabela (quantidade, menor, maior, média)

### Classe `Registro`
Elemento básico armazenado na tabela hash:
- **Encadeamento**: Suporte para listas encadeadas (`proximo`)
- **Codificação**: Conversão automática de inteiro para string de 9 dígitos
- **Flexibilidade**: Usado por todas as estratégias de hash

## Estratégias de Hash Implementadas

### 1. Hash com Encadeamento (`HashEncadeamento.java`)
- **Função Hash**: `(dado ^ (dado >>> 16)) % modulo` com tratamento de negativo
- **Tratamento de Colisões**: Listas encadeadas em cada bucket
- **Vantagens**:
    - Capacidade ilimitada de elementos
    - Não requer redimensionamento
    - Simplicidade de implementação
- **Métricas Especiais**: Identifica as 3 maiores listas encadeadas

### 2. Hash Duplo (`HashDuplo.java`)
- **Função Hash 1**: `(dado ^ (dado >>> 16)) % modulo`
- **Função Hash 2**: `1 + (dado % (tamanhoTabelaHash - 1))`
- **Estratégia de Resolução**: `hash = hash1 + tentativa * hash2`
- **Otimizações**:
    - Limite de 90% de carga para evitar degradação
    - Número máximo de tentativas = tamanhoTabelaHash / 2
    - Tratamento robusto de overflow/underflow

### 3. Hash com Rehashing Quadrático (`HashRehashing.java`)
- **Função Hash Principal**: `(dado ^ (dado >>> 16)) % modulo`
- **Função Rehash**: `hash1 + tentativa²` (incremento quadrático)
- **Características**:
    - Limite de 90% de carga
    - Tratamento de duplicatas durante inserção
    - Busca com limite de tentativas

## Parâmetros e Configuração Experimental

### Tamanhos de Tabela Hash
- **Pequeno**: 100.000 posições
- **Médio**: 1.000.000 posições
- **Grande**: 10.000.000 posições

### Conjuntos de Dados
- **Pequeno**: 100.000 registros
- **Médio**: 1.000.000 registros
- **Grande**: 10.000.000 registros

**Configuração Experimental**:
- **Seed**: `2025` (garante reproduibilidade)
- **Faixa de Dados**: Inteiros de 1 a 2.147.483.647
- **Ambiente**: Java SE com medição de alta precisão

## Métricas Coletadas

### 1. Desempenho Temporal
- **Tempo de Inserção**: Medido em nanossegundos com `System.nanoTime()`
- **Tempo de Busca**: Inclui todas as operações de busca no conjunto de dados

### 2. Eficiência de Colisão
- **Colisões Totais**: Diferente para cada estratégia:
    - **Encadeamento**: Elementos adicionais em buckets + travessias
    - **Hash Duplo/Rehashing**: Tentativas de rehashing necessárias

### 3. Análise de Distribuição
- **Gaps**: Sequências de posições vazias consecutivas
    - Quantidade total de gaps
    - Menor e maior gap encontrado
    - Média do tamanho dos gaps
- **Listas Encadeadas**: Para encadeamento, tamanho das 3 maiores listas

### 4. Eficácia de Busca
- **Buscas Bem-sucedidas**: Elementos encontrados na tabela
- **Buscas Mal-sucedidas**: Elementos não encontrados

## Metodologia de Teste

### Processo Experimental
1. **Preparação**: Geração de dados determinística com seed fixa
2. **Execução**: Para cada combinação (tamanho tabela × tamanho dados × estratégia hash):
    - Limpeza completa da tabela hash
    - Inserção com coleta de estatísticas
    - Busca completa do conjunto de dados
3. **Coleta**: Medições precisas de tempo e contadores
4. **Exportação**: Dados automaticamente salvos em CSV para análise

### Variáveis Controladas
- Mesmos conjuntos de dados para todas as estratégias
- Ambiente de execução consistente
- Limpeza adequada entre testes
- Tratamento uniforme de casos especiais (sem gaps, etc.)

---

## Resultados Detalhados Baseados nos Dados Empíricos

### Análise por Tamanho de Tabela Hash

#### Tabela Pequena (100.000 posições)

![Tempo de Inserção - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_tempoInsercao.png)
![Tempo de Busca - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_tempoBusca.png)
![Colisões - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_colisoes.png)
![Maior Gap - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_maiorGap.png)
![Menor Gap - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_menorGap.png)
![Media Gap - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_mediaGap.png)

**Resultados Empíricos Detalhados**

### Tabela 100.000 posições com 100.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 41.35ms
- **HashEncadeamento**: 16.92ms
- **HashDuplo**: 18.66ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 20.77ms
- **HashEncadeamento**: 4.24ms
- **HashDuplo**: 10.58ms

**Colisões**:
- **HashRehashing**: 159.238 colisões
- **HashEncadeamento**: 99.215 colisões
- **HashDuplo**: 168.697 colisões

**Maior Gap**:
- **HashRehashing**: 6 posições
- **HashEncadeamento**: 11 posições
- **HashDuplo**: 5 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 1.21 posições
- **HashEncadeamento**: 1.58 posições
- **HashDuplo**: 1.11 posições

### Tabela 100.000 posições com 1.000.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 27.19ms
- **HashEncadeamento**: 626.12ms
- **HashDuplo**: 9.70ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 233.44ms
- **HashEncadeamento**: 147.15ms
- **HashDuplo**: 114.14ms

**Colisões**:
- **HashRehashing**: 159.790 colisões
- **HashEncadeamento**: 9.989.186 colisões
- **HashDuplo**: 140.452 colisões

**Maior Gap**:
- **HashRehashing**: 7 posições
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 5 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 1.21 posições
- **HashEncadeamento**: 1.00 posição
- **HashDuplo**: 1.11 posições

### Tabela 100.000 posições com 10.000.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 19.05ms
- **HashEncadeamento**: 27085.93ms
- **HashDuplo**: 10.21ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 917.44ms
- **HashEncadeamento**: 18883.33ms
- **HashDuplo**: 1171.88ms

**Colisões**:
- **HashRehashing**: 161.798 colisões
- **HashEncadeamento**: 996.848.624 colisões
- **HashDuplo**: 168.167 colisões

**Maior Gap**:
- **HashRehashing**: 8 posições
- **HashEncadeamento**: 0 posições
- **HashDuplo**: 5 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 0 posições
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 1.20 posições
- **HashEncadeamento**: 0.00 posições
- **HashDuplo**: 1.11 posições

## Análise de Performance Comparativa Tabela 100.000

### Desempenho por Cenário de Carga

No cenário balanceado, onde a tabela possui 100.000 posições para 100.000 dados, o Hash por Encadeamento demonstra superioridade indiscutível. Seu tempo de inserção de 16.92 milissegundos e tempo de busca de apenas 4.24 milissegundos representam a melhor performance geral entre todas as implementações testadas. O Hash Duplo apresenta performance equilibrada com 18.66 milissegundos para inserção e 10.58 milissegundos para busca, enquanto o Hash com Rehashing mostra deficiências significativas com 41.35 milissegundos na inserção e 20.77 milissegundos na busca.

Quando submetemos as implementações a um cenário de overload moderado, com 1.000.000 de dados para 100.000 posições, observamos uma mudança drástica no panorama de performance. O Hash Duplo assume a liderança com notáveis 9.70 milissegundos para inserção e 114.14 milissegundos para busca. O Hash com Rehashing mostra melhoria relativa na inserção, reduzindo para 27.19 milissegundos. Entretanto, o Hash por Encadeamento sofre um colapso catastrófico na performance, com tempo de inserção explodindo para 626.12 milissegundos - aproximadamente 37 vezes mais lento que no cenário balanceado.

No cenário de overload extremo, com 10.000.000 de dados para as mesmas 100.000 posições, o Hash Duplo mantém sua estabilidade com 10.21 milissegundos para inserção. O Hash com Rehashing continua apresentando performance consistente na inserção com 19.05 milissegundos. O Hash por Encadeamento, no entanto, experimenta uma degradação inaceitável, atingindo 27.085,93 milissegundos na inserção e 18.883,33 milissegundos na busca, tornando-se praticamente inutilizável para aplicações que demandam performance.

### Análise de Escalabilidade e Comportamento

A análise de escalabilidade revela padrões distintos entre as implementações. O Hash por Encadeamento, embora excelente em cenários balanceados, sofre de degradação exponencial sob carga elevada. Seu número de colisões aumenta dramaticamente de 99.215 para 996.848.624, demonstrando que as listas encadeadas tornam-se excessivamente longas e ineficientes sob condições de overload.

O Hash Duplo emerge como a implementação mais escalável, mantendo performance consistente across todos os cenários. Suas colisões permanecem estáveis em torno de 168.000, independentemente do nível de overload, graças à dupla função hash que distribui a carga de maneira mais uniforme pela tabela.

O Hash com Rehashing apresenta um comportamento interessante: enquanto sua performance de inserção melhora com o aumento do overload, o tempo de busca degrada significativamente, indo de 20.77 milissegundos para 917.44 milissegundos no cenário de overload extremo.

### Análise de Distribuição e Eficiência

A distribuição espacial analisada através dos gaps revela que o Hash Duplo mantém a distribuição mais consistente, com gap médio de aproximadamente 1.11 posições em todos os cenários. O Hash por Encadeamento tende a uma distribuição perfeita sob alta carga, com gap médio chegando a zero no cenário de overload extremo, embora essa distribuição ideal não se traduza em boa performance devido ao custo do tratamento de colisões.

#### Tabela Média (1.000.000 posições)

![Tempo de Inserção - 1000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000000_tempoInsercao.png)
![Tempo de Busca - 1000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000000_tempoBusca.png)
![Colisões - 1000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000000_colisoes.png)
![Maior Gap - 1000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000000_maiorGap.png)
![Menor Gap - 1000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000000_menorGap.png)
![Media Gap - 1000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000000_mediaGap.png)

**Resultados Empíricos Detalhados - Tabela 1.000.000 posições**

### Tabela 1.000.000 posições com 100.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 9.52ms
- **HashEncadeamento**: 5.14ms
- **HashDuplo**: 7.83ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 7.08ms
- **HashEncadeamento**: 1.55ms
- **HashDuplo**: 2.49ms

**Colisões**:
- **HashRehashing**: 5.507 colisões
- **HashEncadeamento**: 9.927 colisões
- **HashDuplo**: 5.341 colisões

**Maior Gap**:
- **HashRehashing**: 116 posições
- **HashEncadeamento**: 116 posições
- **HashDuplo**: 109 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 10.46 posições
- **HashEncadeamento**: 10.51 posições
- **HashDuplo**: 10.00 posições

### Tabela 1.000.000 posições com 1.000.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 217.59ms
- **HashEncadeamento**: 124.44ms
- **HashDuplo**: 111.11ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 206.01ms
- **HashEncadeamento**: 44.59ms
- **HashDuplo**: 110.05ms

**Colisões**:
- **HashRehashing**: 1.610.524 colisões
- **HashEncadeamento**: 995.976 colisões
- **HashDuplo**: 1.412.527 colisões

**Maior Gap**:
- **HashRehashing**: 8 posições
- **HashEncadeamento**: 15 posições
- **HashDuplo**: 6 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 1.21 posições
- **HashEncadeamento**: 1.58 posições
- **HashDuplo**: 1.11 posições

### Tabela 1.000.000 posições com 10.000.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 137.89ms
- **HashEncadeamento**: 6008.74ms
- **HashDuplo**: 123.94ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 2191.87ms
- **HashEncadeamento**: 2672.24ms
- **HashDuplo**: 2154.29ms

**Colisões**:
- **HashRehashing**: 1.613.104 colisões
- **HashEncadeamento**: 99.669.638 colisões
- **HashDuplo**: 1.431.484 colisões

**Maior Gap**:
- **HashRehashing**: 9 posições
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 6 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 1.20 posições
- **HashEncadeamento**: 1.00 posição
- **HashDuplo**: 1.11 posições

## Análise de Performance Comparativa Tabela 1.000.000

### Desempenho por Cenário de Carga

No cenário de baixa densidade, onde a tabela possui 1.000.000 de posições para apenas 100.000 dados, o Hash por Encadeamento demonstra performance superior tanto na inserção (5.14ms) quanto na busca (1.55ms). O Hash Duplo apresenta performance intermediária com 7.83ms para inserção e 2.49ms para busca, enquanto o Hash com Rehashing mostra os piores resultados com 9.52ms na inserção e 7.08ms na busca.

No cenário balanceado ideal, com 1.000.000 de dados para 1.000.000 de posições, observamos uma distribuição mais equilibrada de performance. O Hash por Encadeamento mantém sua liderança na busca com impressionantes 44.59ms, enquanto na inserção o Hash Duplo assume vantagem com 111.11ms contra 124.44ms do Encadeamento e 217.59ms do Rehashing.

Quando submetemos as implementações a um cenário de overload (10.000.000 dados para 1.000.000 posições), o panorama muda drasticamente. O Hash Duplo emerge como o mais eficiente na inserção com apenas 123.94ms, seguido pelo Hash com Rehashing com 137.89ms. O Hash por Encadeamento sofre uma degradação catastrófica, com tempo de inserção explodindo para 6008.74ms - aproximadamente 48 vezes mais lento que o Hash Duplo.

### Análise de Escalabilidade e Comportamento

A análise de escalabilidade na tabela de 1.000.000 posições revela padrões consistentes com a análise anterior, porém com algumas particularidades importantes. O Hash por Encadeamento mantém sua excelente performance em cenários de baixa e média densidade, mas demonstra completa falta de escalabilidade sob condições de overload extremo.

O Hash Duplo consolida-se como a implementação mais consistente across todos os cenários, especialmente na inserção onde mantém tempos estáveis mesmo sob carga dez vezes superior à capacidade da tabela. Sua performance de 123.94ms no cenário de overload é notavelmente próxima à do cenário balanceado (111.11ms).

O Hash com Rehashing apresenta um comportamento peculiar: seu tempo de inserção melhora significativamente do cenário balanceado (217.59ms) para o cenário de overload (137.89ms), sugerindo que o mecanismo de rehashing torna-se mais eficiente quando a tabela está sob alta pressão.

### Análise de Distribuição e Eficiência

A distribuição espacial analisada através dos gaps revela padrões interessantes. No cenário de baixa densidade, todos os métodos apresentam gaps médios elevados (em torno de 10 posições), refletindo a dispersão natural em uma tabela pouco ocupada.

À medida que a densidade aumenta, os gaps médios convergem para valores próximos de 1, indicando distribuição quase ideal. O Hash por Encadeamento atinge distribuição perfeita (gap médio de 1.00) no cenário de overload, enquanto o Hash Duplo mantém consistência notável com gap médio de 1.11 em todos os cenários de média e alta densidade.

O número de colisões segue padrão esperado: o Hash por Encadeamento apresenta menor número de colisões no cenário balanceado, mas sofre aumento exponencial sob overload (99.6 milhões de colisões). Já o Hash Duplo e Rehashing mantêm números de colisões relativamente estáveis independentemente da carga.

### Conclusões Específicas para Tabela 1.000.000

A tabela maior (1.000.000 vs 100.000 posições) demonstra melhor capacidade de absorver carga sem degradação severa de performance. Enquanto na tabela menor o Hash por Encadeamento tornava-se inutilizável sob overload, na tabela maior ele ainda mantém performance aceitável na busca mesmo com carga 10x superior.

O Hash Duplo confirma sua superioridade como solução mais balanceada, oferecendo boa performance em todos os cenários e excelente escalabilidade. O Hash com Rehashing mostra melhorias significativas em relação à tabela menor, especialmente na inserção sob alta carga.

Esta análise reforça que a escolha do método de hash ideal depende criticamente do cenário de uso esperado e da relação entre tamanho da tabela e volume de dados a ser armazenado.

#### Tabela Grande (10.000.000 posições)

![Tempo de Inserção - 10000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000000_tempoInsercao.png)
![Tempo de Busca - 10000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000000_tempoBusca.png)
![Colisões - 10000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000000_colisoes.png)
![Maior Gap - 10000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000000_maiorGap.png)
![Menor Gap - 10000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000000_menorGap.png)
![Media Gap - 10000000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000000_mediaGap.png)

**Resultados Empíricos Detalhados - Tabela 10.000.000 posições**

### Tabela 10.000.000 posições com 100.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 10.22ms
- **HashEncadeamento**: 14.81ms
- **HashDuplo**: 10.58ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 2.26ms
- **HashEncadeamento**: 1.91ms
- **HashDuplo**: 2.87ms

**Colisões**:
- **HashRehashing**: 464 colisões
- **HashEncadeamento**: 921 colisões
- **HashDuplo**: 464 colisões

**Maior Gap**:
- **HashRehashing**: 1.191 posições
- **HashEncadeamento**: 1.191 posições
- **HashDuplo**: 1.191 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 100.48 posições
- **HashEncadeamento**: 100.49 posições
- **HashDuplo**: 100.02 posições

### Tabela 10.000.000 posições com 1.000.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 162.40ms
- **HashEncadeamento**: 161.53ms
- **HashDuplo**: 205.18ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 33.93ms
- **HashEncadeamento**: 28.16ms
- **HashDuplo**: 56.63ms

**Colisões**:
- **HashRehashing**: 54.828 colisões
- **HashEncadeamento**: 99.328 colisões
- **HashDuplo**: 53.403 colisões

**Maior Gap**:
- **HashRehashing**: 139 posições
- **HashEncadeamento**: 139 posições
- **HashDuplo**: 139 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 10.46 posições
- **HashEncadeamento**: 10.52 posições
- **HashDuplo**: 10.01 posições

### Tabela 10.000.000 posições com 10.000.000 dados:

**Tempo de Inserção (ms)**:
- **HashRehashing**: 2.439.35ms
- **HashEncadeamento**: 2.165.39ms
- **HashDuplo**: 2.839.34ms

**Tempo de Busca (ms)**:
- **HashRehashing**: 1.933.63ms
- **HashEncadeamento**: 659.90ms
- **HashDuplo**: 2.605.37ms

**Colisões**:
- **HashRehashing**: 16.091.601 colisões
- **HashEncadeamento**: 9.949.762 colisões
- **HashDuplo**: 15.007.236 colisões

**Maior Gap**:
- **HashRehashing**: 11 posições
- **HashEncadeamento**: 15 posições
- **HashDuplo**: 7 posições

**Menor Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 1 posição
- **HashDuplo**: 1 posição

**Média Gap**:
- **HashRehashing**: 1.20 posições
- **HashEncadeamento**: 1.58 posições
- **HashDuplo**: 1.11 posições

## Análise de Performance Comparativa Tabela 10.000.000

### Desempenho por Cenário de Carga

No cenário de baixa densidade (100.000 dados em 10 milhões de posições), observamos uma distribuição interessante de performance. O Hash por Encadeamento demonstra a melhor eficiência na busca com 1.91ms, enquanto HashRehashing e HashDuplo apresentam performance muito similar na inserção (≈10.4ms). O HashDuplo mostra ligeira desvantagem na busca com 2.87ms.

No cenário de média densidade (1 milhão de dados), as três implementações apresentam performance bastante equilibrada na inserção, com HashEncadeamento (161.53ms) e HashRehashing (162.40ms) virtualmente empatados, e HashDuplo ligeiramente atrás (205.18ms). Na busca, o HashEncadeamento mantém liderança com 28.16ms, seguido por HashRehashing (33.93ms) e HashDuplo (56.63ms).

No cenário de alta densidade (tabela completamente preenchida com 10 milhões de dados), o HashEncadeamento emerge como claro vencedor, alcançando o melhor desempenho tanto na inserção (2.165.39ms) quanto na busca (659.90ms). O HashRehashing apresenta performance intermediária, enquanto o HashDuplo demonstra os piores resultados neste cenário específico.

### Análise de Escalabilidade e Comportamento

A tabela de 10 milhões de posições revela padrões de escalabilidade distintos dos observados nas tabelas menores. Diferente do comportamento anterior, onde o HashDuplo se destacava sob overload, nesta configuração maior o HashEncadeamento demonstra excelente escalabilidade, mantendo performance superior mesmo quando a tabela está completamente preenchida.

O HashRehashing mostra comportamento consistente, com degradação gradual de performance conforme a densidade aumenta. Seu tempo de inserção cresce de forma aproximadamente linear com o aumento da carga.

O HashDuplo, que anteriormente se mostrava como a solução mais escalável, nesta configuração maior apresenta a pior performance no cenário de alta densidade, sugerindo que seu mecanismo de hash duplo pode sofrer com clusters de colisões em tabelas muito grandes quando completamente preenchidas.

### Análise de Distribuição e Eficiência

A análise de distribuição espacial na tabela gigante revela padrões fascinantes. Nos cenários de baixa e média densidade, os gaps médios são extremamente elevados (100.48 e 10.46 posições respectivamente para HashRehashing), refletindo a enorme dispersão em uma tabela esparsamente povoada.

Conforme a tabela se aproxima da capacidade total, os gaps convergem para valores próximos de 1, indicando distribuição quase ideal. O HashDuplo mantém sua característica de melhor distribuição com gap médio de 1.11 posições, seguido pelo HashRehashing (1.20) e HashEncadeamento (1.58).

O número de colisões segue padrão consistente: HashEncadeamento apresenta significativamente menos colisões (9.9 milhões) no cenário de alta densidade comparado aos outros métodos (16.1 e 15.0 milhões), o que explica sua performance superior neste cenário específico.

### Conclusões Específicas para Tabela 10.000.000

A tabela de 10 milhões de posições apresenta dinâmica fundamentalmente diferente das tabelas menores. O enorme espaço disponível permite que o HashEncadeamento brilhe, especialmente em cenários de alta densidade onde sua eficiência no tratamento de colisões através de listas encadeadas se mostra superior.

A performance relativa dos métodos inverte-se em comparação com tabelas menores: enquanto em tabelas pequenas sob overload o HashDuplo era superior, na tabela gigante completamente preenchida o HashEncadeamento domina claramente.

Esta análise demonstra que o tamanho absoluto da tabela hash é um fator crítico na escolha do método ideal. Para aplicações que requerem tabelas muito grandes com alta taxa de ocupação, o Hash por Encadeamento pode ser a escolha mais eficiente, enquanto para tabelas menores ou com flutuações significativas de carga, o Hash Duplo mantém vantagens em termos de consistência.

O HashRehashing posiciona-se como uma solução balanceada, oferecendo performance competitiva dentre todos os cenários sem se destacar em nenhum específico, representando uma escolha segura para aplicações com requisitos variáveis.

### Análise Comparativa Geral

![Comparativo Tempo de Inserção](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_tempoInsercao.png)
![Comparativo Tempo de Busca](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_tempoBusca.png)
![Comparativo Colisões](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_colisoes.png)
![Comparativo Maior Gap](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_maiorGap.png)
![Comparativo Menor Gap](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_menorGap.png)
![Comparativo Media Gap](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_mediaGap.png)

## Análise de Desempenho Detalhada Baseada em Dados Reais

### Tabela Comparativa de Performance com Dados Empíricos

| Métrica           | Tamanho    | HashRehashing | HashEncadeamento | HashDuplo      | Vencedor       |
|-------------------|------------|---------------|------------------|----------------|----------------|
| **Inserção (ms)** | 100.000    | 29.29         | 9242.99          | **12.86**      | HashDuplo      |
| **Inserção (ms)** | 1.000.000  | 121.67        | 2046.11          | **80.96**      | HashDuplo      |
| **Inserção (ms)** | 10.000.000 | 870.66        | **780.58**       | 1018.37        | Encadeamento   |
| **Busca (ms)**    | 100.000    | **390.55**    | 6344.90          | 432.20         | Rehashing      |
| **Busca (ms)**    | 1.000.000  | 801.65        | 906.13           | **755.61**     | HashDuplo      |
| **Busca (ms)**    | 10.000.000 | 656.61        | **229.99**       | 888.29         | Encadeamento   |
| **Colisões**      | 100.000    | 160275.33     | 335645675.00     | **159105.33**  | HashDuplo      |
| **Colisões**      | 1.000.000  | 1076378.33    | 33558513.67      | **949784.00**  | HashDuplo      |
| **Colisões**      | 10.000.000 | 5382297.67    | 3350003.67       | **5020367.67** | HashDuplo      |
| **Maior Gap**     | 100.000    | 7             | **4**            | 5              | Encadeamento   |
| **Maior Gap**     | 1.000.000  | 44.33         | 44               | **40.33**      | HashDuplo      |
| **Maior Gap**     | 10.000.000 | 447           | 448.33           | **445.67**     | HashDuplo      |
| **Menor Gap**     | 100.000    | 1             | **0.67**         | 1              | Encadeamento   |
| **Menor Gap**     | 1.000.000  | **1**         | **1**            | **1**          | Empate         |
| **Menor Gap**     | 10.000.000 | 447           | 448.33           | **445.67**     | HashDuplo      |
| **Média Gap**     | 100.000    | 1.21          | **0.86**         | 1.11           | Encadeamento   |
| **Média Gap**     | 1.000.000  | 4.29          | 4.36             | **4.07**       | HashDuplo      |
| **Média Gap**     | 10.000.000 | 37.38         | 37.53            | **37.05**      | HashDuplo      |

## Análise de Comportamento por Fator de Carga

**Fator de Carga (α) = n/m**

#### α = 1.0 (100.000 elementos em 100.000 posições)
- **HashDuplo**: Excelente performance em inserção (12.86 ms)
- **HashRehashing**: Performance sólida em inserção (29.29 ms) e melhor em busca para 100k (390.55 ms)
- **Encadeamento**: Performance lenta em inserção (9242.99 ms) mas competitiva em busca para alta escala
- **Colisões**: HashDuplo com menor número (159k vs 160k vs 335M)

#### α = 10.0 (1.000.000 elementos em 100.000 posições)
- **HashDuplo**: Mantém vantagem em inserção (80.96 ms) e busca (755.61 ms)
- **HashRehashing**: Performance consistente (121.67 ms inserção, 801.65 ms busca)
- **Encadeamento**: Melhora performance relativa (2046.11 ms inserção)
- **Colisões**: HashDuplo mantém vantagem (949k vs 1M vs 33M)

#### α = 100.0 (10.000.000 elementos em 100.000 posições)
- **Encadeamento**: Torna-se dominante em inserção (780.58 ms) e busca (229.99 ms)
- **HashRehashing**: Performance sólida (870.66 ms inserção, 656.61 ms busca)
- **HashDuplo**: Degradação significativa (1018.37 ms inserção, 888.29 ms busca)
- **Colisões**: Todas as estratégias mostram aumento significativo, HashDuplo ainda lidera

## Análise de Comportamentos Anômalos e Insights Críticos

### Performance Bimodal do Encadeamento
O encadeamento demonstrou comportamento bimodal extremo:
- **Baixa/Média Carga**: Performance muito inferior (9242.99 ms para 100k vs 12.86 ms do HashDuplo)
- **Alta Carga**: Performance superior (780.58 ms para 10M vs 1018.37 ms do HashDuplo)

**Interpretação**: Overhead inicial de alocação de nós é significativo, mas escala melhor sob alta carga.

### Consistência do HashRehashing
HashRehashing mostrou a **performance mais equilibrada** através de todas as cargas:
- Variação de inserção: 29.29 ms to 870.66 ms (30×)
- Variação de busca: 390.55 ms to 801.65 ms (2×)
- Compare com HashDuplo: 12.86 ms to 1018.37 ms (79×)
- Compare com Encadeamento: 780.58 ms to 9242.99 ms (12×)

### Evolução dos Padrões de Distribuição (Gaps)
A análise dos gaps revela padrões importantes:

| Tamanho    | HashRehashing | HashEncadeamento | HashDuplo | Interpretação                         |
|------------|---------------|------------------|-----------|---------------------------------------|
| 100.000    | 7             | 4                | 5         | HashEncadeamento é levemente superior |
| 1.000.000  | 44.33         | 44               | 40.33     | HashDuplo com melhor distribuição     |
| 10.000.000 | 447           | 448.33           | 445.67    | HashDuplo mantém vantagem marginal    |

## Análise de Complexidade Prática vs Teórica

### Hash com Encadeamento
- **Complexidade Teórica**: O(1 + α) onde α = n/m
- **Complexidade Prática Observada**: O(α) para α > 10, performance inicial fraca
- **Vantagem Prática**: Excelente escalabilidade sob alta carga
- **Desvantagem**: Overhead significativo em carga baixa/média

### Hash com Rehashing
- **Complexidade Teórica**: O(1/(1-α)) para α < 1.0
- **Complexidade Prática Observada**: Performance mais consistente através de diferentes cargas
- **Vantagem Prática**: Bom equilíbrio entre performance e previsibilidade
- **Comportamento**: Menor variabilidade entre diferentes cenários

### Hash Duplo
- **Complexidade Teórica**: Similar ao rehashing, mas com melhor distribuição
- **Complexidade Prática Observada**: Excelente performance em carga baixa/média, degradação em alta carga
- **Vantagem Prática**: Melhor performance geral para α ≤ 10
- **Risco Prático**: Degradação sob carga muito alta

## Análise de Trade-offs e Recomendações Baseadas em Evidências

### Para Aplicações com Carga Baixa/Média (α ≤ 10)
**Recomendação**: HashDuplo
- **Justificativa Empírica**: Performance superior em inserção (12.86 ms para 100k) e busca (755.61 ms para 1M)
- **Cenário Ideal**: Sistemas onde o fator de carga é controlável
- **Vantagem**: Menor número de colisões em todos os cenários

### Para Aplicações com Carga Muito Alta (α ≥ 50)
**Recomendação**: Hash com Encadeamento
- **Justificativa Empírica**: Performance superior em alta carga (780.58 ms inserção, 229.99 ms busca para 10M)
- **Cenário Ideal**: Sistemas com carga imprevisível ou muito alta
- **Vantagem**: Melhor escalabilidade sob condições extremas

### Para Aplicações que Requerem Consistência
**Recomendação**: Hash com Rehashing
- **Justificativa Empírica**: Performance mais previsível (menor variabilidade)
- **Cenário Ideal**: Sistemas em tempo real, aplicações com SLAs rigorosos
- **Vantagem**: Nunca o pior performer, frequentemente competitivo

## Lições Aprendidas e Insights Críticos Baseados em Dados

### 1. Não Existe Solução Universal
**Insight**: Cada estratégia tem seu nicho de excelência baseado no fator de carga esperado.

**Evidência**:
- HashDuplo: 6 vitórias em carga baixa/média
- Encadeamento: 2 vitórias em carga muito alta
- Rehashing: 1 vitória e múltiplos segundos lugares

### 2. O Mito do "Melhor Algoritmo"
**Insight**: A performance relativa muda dramaticamente com a escala.

**Evidência**:
- HashDuplo: de 12.86 ms (100k) para 1018.37 ms (10M) - 79× mais lento
- Encadeamento: de 9242.99 ms (100k) para 780.58 ms (10M) - 12× mais rápido
- Rehashing: performance mais estável (30× variação vs 79× do HashDuplo)

### 3. Colisões Não Correlacionam Diretamente com Performance
**Insight**: Menor número de colisões não garante melhor performance geral.

**Evidência**:
- HashDuplo: menor colisões em todos os cenários
- Encadeamento: mais colisões mas melhor performance em alta carga
- Fator decisivo: custo de tratamento de colisões, não número absoluto

### 4. Importância do Caso de Uso Específico
**Insight**: A escolha ideal depende inteiramente do padrão de uso esperado.

**Evidência**:
- Operações predominantes de inserção: HashDuplo
- Carga muito alta: Encadeamento
- Consistência: Rehashing

## Conclusões Finais Baseadas em Análise Empírica

### Performance Geral por Categoria

**Melhor Performance em Carga Baixa/Média**: HashDuplo
- **Vencedor em**: 6 das 9 categorias de tempo para tamanhos ≤ 1M
- **Vantagem principal**: Inserção rápida e menor número de colisões
- **Cenários vencidos**: Inserção (2/3), Busca (1/3), Colisões (3/3)

**Melhor Performance em Alta Carga**: Hash com Encadeamento
- **Vencedor em**: 2 das 3 categorias para 10M elementos
- **Vantagem principal**: Escalabilidade superior sob carga extrema
- **Cenários vencidos**: Inserção alta carga, Busca alta carga

**Melhor Consistência**: Hash com Rehashing
- **Performance**: Mais previsível através de diferentes cargas
- **Vantagem**: Nunca o pior caso, frequentemente competitivo
- **Adequado para**: Sistemas que requerem comportamento previsível

### Tabela de Recomendações Finais Baseada em Evidências

| Cenário de Carga         | Recomendação | Justificativa Baseada em Dados | Performance Esperada  |
|--------------------------|--------------|--------------------------------|-----------------------|
| **α ≤ 10**               | HashDuplo    | Melhor performance geral       | 12.86ms inserção/100k |
| **α ≥ 50**               | Encadeamento | Melhor escalabilidade          | 780.58ms inserção/10M |
| **Carga Variável**       | Rehashing    | Performance mais previsível    | 29-870ms inserção     |
| **Operações Críticas**   | Rehashing    | Menor risco de degradação      | Variação 2× em busca  |
| **Restrição de Memória** | HashDuplo    | Menor overhead estrutural      | -                     |

### Recomendação Final

Para a maioria das aplicações práticas onde o fator de carga pode ser mantido abaixo de 10, **recomenda-se o HashDuplo** devido à sua performance superior geral e menor número de colisões. Para aplicações com carga muito alta ou imprevisível, **o Encadeamento é preferível** devido à sua melhor escalabilidade. O Hash com Rehashing representa uma escolha conservadora para aplicações que exigem comportamento previsível e consistente.

**Fator decisivo**: A capacidade de prever e controlar o fator de carga máximo na aplicação específica.

## Como Reproduzir os Experimentos

### Pré-requisitos
```bash
# Java
java -version  # JDK 8+ requerido

# Python (para análise)
python --version  # Python 3.6+ requerido
```

### Execução Completa
Entrar no IntelliJ e rodar a função `main` no arquivo [`Main.java`](src/Main.java)
```bash
# 1. Gerar gráficos e análise
python -m venv .venv
source .venv/bin/activate
ou no windows
.venv/Scripts/activate.bat

cd plots
pip install -r requirements.txt
python plot.py
```

### Estrutura de Saída
```
plots/
├── 1000/
│   ├── tempoInsercao.csv
│   ├── tempoBusca.csv
│   ├── colisoes.csv
│   └── maiorGap.csv
├── 10000/
│   └── ...
├── 100000/
│   └── ...
└── plots/
    ├── [gráficos por tamanho]
    └── [gráficos comparativos]
```

## Autores

Renan da Silva Oliveira Andrade (renan.silva3@pucpr.edu.br)

Ricardo Lucas Kucek (ricardo.kucek@pucpr.edu.br)

## Tecnologias Utilizadas

- **Java SE**: Implementação das tabelas hash
- **Matplotlib**: Geração de gráficos
- **Pandas**: Análise de dados
- **NumPy**: Cálculos estatísticos

---

*Este trabalho compõe a nota do RA3 da disciplina de Estruturas de Dados. Implementado seguindo rigorosamente as especificações fornecidas, com análise empírica abrangente baseada em dados reais coletados experimentalmente. Os resultados revelaram insights valiosos sobre o comportamento prático de diferentes estratégias de tabelas hash em cenários do mundo real.*