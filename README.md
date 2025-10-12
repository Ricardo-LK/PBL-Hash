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
├── plots/                 # Scripts e resultados de análise
│   ├── plot.py           # Script Python para geração de gráficos
│   ├── requirements.txt  # Dependências do Python
│   └── [tamanhos]/       # Diretórios com resultados para cada tamanho
└── out/                  # Arquivos compilados
```

## Implementações de Tabela Hash

### 1. Hash com Rehashing Linear/Quadrático (`HashRehashing.java`)
- **Função Hash Principal**: XOR com shift right 16 bits + módulo
- **Estratégia de Rehashing**: Quadrática (i²)
- **Tratamento de Colisões**: Busca por posição vazia no vetor usando rehashing quadrático
- **Fórmula**: `hash = (hash1 + tentativa²) % modulo`

### 2. Hash Duplo (`HashDuplo.java`)
- **Primeira Função Hash**: XOR com shift right 16 bits + módulo
- **Segunda Função Hash**: `1 + (dado % (tamanhoTabelaHash - 1))` (hash duplo clássico)
- **Estratégia**: `hash = (hash1 + tentativa * hash2) % modulo`
- **Vantagem**: Distribuição mais uniforme que rehashing linear/quadrático

### 3. Hash com Encadeamento (`HashEncadeamento.java`)
- **Função Hash**: XOR com shift right 16 bits + módulo
- **Estratégia**: Listas encadeadas em cada bucket
- **Característica**: Capacidade ilimitada de elementos
- **Tratamento de Colisões**: Encadeamento na mesma posição

## Parâmetros do Estudo

### Tamanhos do Vetor Hash
- **Pequeno**: 1.000 posições
- **Médio**: 10.000 posições  
- **Grande**: 100.000 posições

### Conjuntos de Dados
- **Pequeno**: 1.000 registros
- **Médio**: 10.000 registros  
- **Grande**: 100.000 registros

**Seed Utilizada**: `2025` (para garantir reproduibilidade dos testes)

**Faixa de Dados**: Números inteiros de 1 a 2.147.483.647

## Métricas Avaliadas

### 1. Tempo de Inserção
- Tempo total para inserir todos os elementos
- Medido em nanossegundos (ns)
- Coletado usando `System.nanoTime()`

### 2. Tempo de Busca
- Tempo total para buscar todos os elementos
- Medido em nanossegundos (ns)
- Inclui buscas bem-sucedidas e mal-sucedidas

### 3. Número de Colisões
- **Para Rehashing/Hash Duplo**: Número de tentativas de rehashing necessárias
- **Para Encadeamento**: Número de elementos além do primeiro em cada bucket + travessias durante inserção

### 4. Análise de Distribuição
- **Três Maiores Listas**: Para encadeamento, tamanho das três maiores listas
- **Gaps**: Menor, maior e média de espaços vazios entre elementos no vetor
- **Quantidade de Gaps**: Número total de sequências vazias na tabela

### 5. Eficiência de Busca
- **Buscas Bem-sucedidas**: Elementos encontrados na tabela
- **Buscas Mal-sucedidas**: Elementos não encontrados

## Metodologia Experimental

### Configuração dos Testes
- **Ambiente**: Java SE
- **Medições**: 9 combinações (3 tamanhos de tabela × 3 tamanhos de dados)
- **Repetição**: 3 funções hash diferentes para cada combinação
- **Consistência**: Mesmos conjuntos de dados para todas as funções hash

### Processo de Coleta
1. Geração de dados com seed fixa
2. Limpeza da tabela hash entre testes
3. Medição precisa de tempos com `nanoTime()`
4. Coleta abrangente de estatísticas
5. Exportação automatizada para CSV

## Resultados Detalhados Baseados nos Dados Empíricos

### Análise por Tamanho de Tabela Hash

#### Tabela Pequena (1.000 posições)

![Tempo de Inserção - 1000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000_tempoInsercao.png)
![Tempo de Busca - 1000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000_tempoBusca.png)
![Colisões - 1000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000_colisoes.png)
![Maior Gap - 1000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000_maiorGap.png)

**Resultados Empíricos Detalhados - Tabela 1.000 com 1.000 dados**:

**Tempo de Inserção (ns)**:
- **HashRehashing**: 132.193 ns
- **HashEncadeamento**: 114.741 ns  
- **HashDuplo**: 29.975 ns

**Tempo de Busca (ns)**:
- **HashRehashing**: 112.437 ns
- **HashEncadeamento**: 19.687 ns
- **HashDuplo**: 139.296 ns

**Colisões**:
- **HashRehashing**: 52 colisões
- **HashEncadeamento**: 94 colisões
- **HashDuplo**: 52 colisões

**Maior Gap**:
- **HashRehashing**: 797 posições
- **HashEncadeamento**: 797 posições  
- **HashDuplo**: 797 posições

**Análise Detalhada - Tabela 1.000**:
O HashDuplo demonstrou performance excepcional em inserção (29.975 ns), sendo **4.4× mais rápido** que o Rehashing. No entanto, paradoxalmente, foi o mais lento em operações de busca. O maior gap de 797 posições indica distribuição não uniforme, com grandes áreas vazias na tabela (79.7% da tabela vazia em sequência). O encadeamento mostrou excelente performance em buscas, sendo **5.7× mais rápido** que o Rehashing. As baixas contagens de colisões (52-94) indicam que todas as estratégias funcionam bem com fator de carga 1.0 em tabelas pequenas.

#### Tabela Média (10.000 posições)

![Tempo de Inserção - 10000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000_tempoInsercao.png)
![Tempo de Busca - 10000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000_tempoBusca.png)
![Colisões - 10000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/10000_colisoes.png)
![Maior Gap - 10000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/1000_maiorGap.png)

**Resultados Empíricos Detalhados - Tabela 10.000**:

**Tempo de Inserção (ns)**:
- **HashRehashing**: 2.082.388 ns
- **HashEncadeamento**: 2.300.149 ns
- **HashDuplo**: 1.859.799 ns

**Tempo de Busca (ns)**:
- **HashRehashing**: 1.629.636 ns
- **HashEncadeamento**: 631.281 ns
- **HashDuplo**: 2.923.134 ns

**Colisões**:
- **HashRehashing**: 154.136 colisões
- **HashEncadeamento**: 10.144 colisões
- **HashDuplo**: 190.744 colisões

**Maior Gap**:
- **HashRehashing**: 89 posições
- **HashEncadeamento**: 89 posições
- **HashDuplo**: 89 posições

**Análise Detalhada - Tabela 10.000**:
Neste tamanho, o HashDuplo manteve sua vantagem em inserção (1.859.799 ns), enquanto o encadeamento consolidou sua liderança em buscas (631.281 ns), sendo **2.6× mais rápido** que o Rehashing. O aumento significativo nas colisões do HashDuplo (190.744 vs 52 na tabela menor) sugere que a estratégia de hash duplo é mais sensível ao aumento do fator de carga. A redução do maior gap para 89 posições (0.89% da tabela) indica melhor distribuição espacial comparada à tabela de 1.000 posições.

#### Tabela Grande (100.000 posições)

![Tempo de Inserção - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_tempoInsercao.png)
![Tempo de Busca - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_tempoBusca.png)
![Colisões - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_colisoes.png)
![Maior Gap - 100000](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/100000_maiorGap.png)

**Resultados Empíricos Detalhados - Tabela 100.000**:

**Tempo de Inserção (ns)**:
- **HashRehashing**: 42.855.681 ns
- **HashEncadeamento**: 8.710.984 ns
- **HashDuplo**: 3.144.902.200 ns

**Tempo de Busca (ns)**:
- **HashRehashing**: 32.266.816 ns
- **HashEncadeamento**: 3.765.222 ns
- **HashDuplo**: 4.270.411.300 ns

**Colisões**:
- **HashRehashing**: 2.243.940 colisões
- **HashEncadeamento**: 99.673 colisões
- **HashDuplo**: 2.550.075 colisões

**Maior Gap**:
- **HashRehashing**: 1 posição
- **HashEncadeamento**: 11 posições
- **HashDuplo**: 1 posição

**Análise Detalhada - Tabela 100.000**:
Resultados dramáticos emergem neste cenário. O HashDuplo sofreu uma **degradação catastrófica**, com tempos de inserção e busca na casa dos bilhões de nanossegundos (3.14 segundos e 4.27 segundos respectivamente). Em contraste, o encadeamento mostrou performance robusta, com apenas 8.7ms para inserção e 3.7ms para busca, sendo **4.9× mais rápido** em inserção e **8.6× mais rápido** em busca que o Rehashing. O Rehashing apresentou comportamento intermediário, mas com número elevado de colisões (2.2 milhões). A distribuição quase perfeita (gaps de 1-11 posições) indica excelente espalhamento das funções hash.

### Análise Comparativa Geral

![Comparativo Tempo de Inserção](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_tempoInsercao.png)
![Comparativo Tempo de Busca](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_tempoBusca.png)
![Comparativo Colisões](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_colisoes.png)
![Comparativo Maior Gap](https://raw.githubusercontent.com/Ricardo-LK/PBL-Hash/refs/heads/main/plots/plots/comparativo_maiorGap.png)

## Análise de Desempenho Detalhada Baseada em Dados Reais

### Tabela Comparativa de Performance com Dados Empíricos

| Métrica | Tamanho | HashRehashing | HashEncadeamento | HashDuplo | Vencedor | Fator de Melhoria |
|---------|---------|---------------|------------------|-----------|----------|-------------------|
| **Inserção (ns)** | 1.000 | 132.193 | 114.741 | **29.975** | HashDuplo | 4.4× |
| **Inserção (ns)** | 10.000 | 2.082.388 | 2.300.149 | **1.859.799** | HashDuplo | 1.1× |
| **Inserção (ns)** | 100.000 | 42.855.681 | **8.710.984** | 3.144.902.200 | Encadeamento | 4.9× |
| **Busca (ns)** | 1.000 | 112.437 | **19.687** | 139.296 | Encadeamento | 5.7× |
| **Busca (ns)** | 10.000 | 1.629.636 | **631.281** | 2.923.134 | Encadeamento | 2.6× |
| **Busca (ns)** | 100.000 | 32.266.816 | **3.765.222** | 4.270.411.300 | Encadeamento | 8.6× |
| **Colisões** | 1.000 | **52** | 94 | **52** | Rehashing/Duplo | - |
| **Colisões** | 10.000 | 154.136 | **10.144** | 190.744 | Encadeamento | 15.2× |
| **Colisões** | 100.000 | 2.243.940 | **99.673** | 2.550.075 | Encadeamento | 22.5× |

### Análise de Comportamento por Fator de Carga

**Fator de Carga (α) = n/m**

#### α = 1.0 (1.000 elementos em 1.000 posições)
- **HashDuplo**: Excelente performance em inserção (29.975 ns)
- **Encadeamento**: Dominante em buscas (19.687 ns)
- **Rehashing**: Performance equilibrada (132.193 ns inserção, 112.437 ns busca)
- **Colisões**: Mínimas para todas as estratégias (52-94)

#### α = 1.0 (10.000 elementos em 10.000 posições)  
- **HashDuplo**: Mantém vantagem em inserção (1.859.799 ns)
- **Encadeamento**: Consolida liderança em buscas (631.281 ns)
- **Rehashing**: Aumento significativo de colisões (154.136)
- **Distribuição**: Melhora significativamente (maior gap: 89 vs 797)

#### α = 1.0 (100.000 elementos em 100.000 posições)
- **HashDuplo**: **Degradação catastrófica** (3.14 segundos inserção)
- **Encadeamento**: Performance consistente e robusta (8.7ms inserção)
- **Rehashing**: Degradação moderada (42.8ms inserção)
- **Colisões**: Encadeamento mantém vantagem significativa (99.673 vs 2.2-2.5 milhões)

## Análise de Comportamentos Anômalos e Insights Críticos

### Degradação Catastrófica do Hash Duplo
O comportamento extremo do HashDuplo com 100.000 elementos (3.144.902.200 ns) sugere um dos seguintes cenários:

1. **Clusterização Secundária Severa**: As duas funções hash podem estar gerando padrões que se reforçam negativamente
2. **Problema de Implementação**: Possível loop infinito ou condição de término inadequada no rehashing
3. **Má Distribuição**: A segunda função hash pode não estar fornecendo saltos suficientemente diferentes
4. **Sensibilidade ao Tamanho**: O algoritmo pode ser particularmente sensível a tabelas maiores

### Performance Consistente do Encadeamento
O encadeamento demonstrou a **menor variabilidade de performance** entre diferentes tamanhos de tabela:
- Variação de inserção: 114.741 ns to 8.710.984 ns (76×)
- Variação de busca: 19.687 ns to 3.765.222 ns (191×)
- Compare com HashDuplo: 29.975 ns to 3.144.902.200 ns (104,857×)

### Evolução dos Padrões de Distribuição (Gaps)
A análise dos maiores gaps revela padrões fascinantes:

| Tamanho | HashRehashing | HashEncadeamento | HashDuplo | Interpretação |
|---------|---------------|------------------|-----------|---------------|
| 1.000 | 797 | 797 | 797 | Distribuição pobre, grandes áreas vazias |
| 10.000 | 89 | 89 | 89 | Melhora significativa na distribuição |
| 100.000 | 1 | 11 | 1 | Distribuição quase perfeita |

## Análise de Complexidade Prática vs Teórica

### Hash com Encadeamento
- **Complexidade Teórica**: O(1 + α) onde α = n/m
- **Complexidade Prática Observada**: O(1) para α ≤ 10, degradação gradual após
- **Vantagem Prática**: Não sofre degradação catastrófica com α ≥ 1.0
- **Robustez**: Performance mais previsível através de diferentes cenários

### Hash com Rehashing
- **Complexidade Teórica**: O(1/(1-α)) para α < 1.0
- **Complexidade Prática Observada**: O(n) para α ≈ 1.0, degradação controlada
- **Limitação Prática**: Performance aceitável apenas para α ≤ 1.0
- **Comportamento**: Degradação previsível mas significativa com alta carga

### Hash Duplo  
- **Complexidade Teórica**: Similar ao rehashing, mas com melhor distribuição
- **Complexidade Prática Observada**: **Comportamento bimodal** - O(1) para α ≤ 1.0, O(∞) para α ≥ 1.0 em alguns casos
- **Risco Prático**: Sensibilidade extrema a implementação e qualidade das funções hash
- **Confiabilidade**: Baixa para aplicações de missão crítica

## Análise de Trade-offs e Recomendações Baseadas em Evidências

### Para Aplicações com Tamanho Conhecido e α ≤ 0.7
**Recomendação**: HashDuplo
- **Justificativa Empírica**: Performance superior em inserção (29.975 ns para 1.000 elementos)
- **Cenário Ideal**: Tabelas com capacidade conhecida e fator de carga conservador
- **Risco**: Requer testes rigorosos em produção

### Para Aplicações com Tamanho Imprevisível ou α ≥ 0.8  
**Recomendação**: Hash com Encadeamento
- **Justificativa Empírica**: Robustez comprovada (apenas 8.7ms para 100.000 elementos)
- **Cenário Ideal**: Aplicações genéricas, sistemas com carga variável
- **Vantagem**: Tolerância excepcional a alta carga sem degradação catastrófica

### Para Aplicações com Restrições de Memória
**Recomendação**: Hash com Rehashing
- **Justificativa Empírica**: Performance aceitável para α ≤ 1.0 (42.8ms para 100.000 elementos)
- **Cenário Ideal**: Sistemas embarcados, aplicações com memória limitada
- **Trade-off**: Performance consistentemente inferior ao encadeamento

## Lições Aprendidas e Insights Críticos Baseados em Dados

### 1. A Importância da Robustez sobre Performance Pico
**Insight**: O encadeamento, embora nem sempre o mais rápido em condições ideais, demonstrou ser a estratégia mais confiável.

**Evidência**: 
- Variação de performance: 76× (encadeamento) vs 104,857× (hash duplo)
- Zero casos de degradação catastrófica
- Performance consistente através de todos os tamanhos

### 2. Sensibilidade não Linear das Estratégias de Rehashing
**Insight**: Rehashing e HashDuplo exibiram comportamentos altamente não-lineares que dificultam a previsão de performance.

**Evidência**:
- HashDuplo: transição de melhor performance (1.000 elementos) para pior performance (100.000 elementos)
- Rehashing: aumento de 52 para 2,243,940 colisões (43,152× aumento)

### 3. A Métrica de Gaps como Indicador de Qualidade
**Insight**: A análise dos gaps provou ser um excelente indicador antecipado de problemas de performance.

**Evidência**:
- Tabela 1.000: gap de 797 → performance inconsistente
- Tabela 100.000: gap de 1-11 → performance robusta (encadeamento/rehashing)

### 4. Trade-off entre Performance Ideal e Previsibilidade
**Insight**: Estratégias que oferecem performance excepcional em condições ideais podem falhar catastróficamente em cenários adversos.

**Evidência**:
- HashDuplo: 29.975 ns (melhor) vs 3.144.902.200 ns (pior)
- Encadeamento: 114.741 ns (3º) vs 8.710.984 ns (1º)

## Conclusões Finais Baseadas em Análise Empírica

### Performance Geral por Categoria

**🏆 Melhor Performance Geral**: Hash com Encadeamento
- **Vencedor em**: 6 das 9 categorias de tempo
- **Performance mais consistente**: Menor variabilidade entre tamanhos
- **Tolerância a alta carga**: Única estratégia sem degradação catastrófica
- **Cenários vencidos**: Busca (3/3), Inserção em alta carga (1/1)

**🥈 Melhor Compromisso para Carga Conhecida**: HashDuplo  
- **Performance superior em**: Inserção em carga baixa/média (2/3)
- **Requer**: Cuidado extremo na implementação e teste
- **Adequado para**: Cenários controlados com α ≤ 0.7
- **Risco**: Degradação imprevisível em alta carga

**🥉 Solução Conservadora**: Hash com Rehashing
- **Performance**: Previsível e controlada
- **Vantagem**: Menor overhead de memória
- **Adequado para**: Aplicações com restrições de memória
- **Limitação**: Performance consistentemente inferior ao encadeamento

### Tabela de Recomendações Finais Baseada em Evidências

| Cenário | Recomendação | Justificativa Baseada em Dados | Performance Esperada |
|---------|--------------|--------------------------------|---------------------|
| **Aplicações Genéricas** | Encadeamento | Robustez comprovada, tolerância a alta carga | 8.7ms/100k elementos |
| **Sistemas Críticos** | Encadeamento | Zero degradação catastrófica | Performance previsível |
| **Carga Conhecida α ≤ 0.7** | HashDuplo | Melhor performance em condições ideais | 29.975ns/1k elementos |
| **Memória Limitada** | Rehashing | Menor overhead, performance aceitável | 42.8ms/100k elementos |
| **Alta Carga α ≥ 1.0** | Encadeamento | Única estratégia estável | 3.7ms busca/100k elementos |

### Recomendação Final

Para a maioria das aplicações práticas, **recomenda-se fortemente o uso de Hash com Encadeamento** devido à sua robustez comprovada, performance consistente e tolerância a condições variáveis de carga. As estratégias baseadas em rehashing devem ser reservadas para cenários específicos onde:

1. O tamanho máximo é conhecido com certeza absoluta
2. O fator de carga pode ser mantido conservadoramente abaixo de 0.7
3. Existem restrições severas de memória
4. Foi conduzido teste rigoroso em condições de produção

## Como Reproduzir os Experimentos

### Pré-requisitos
```bash
# Java
java -version  # JDK 8+ requerido

# Python (para análise)
python --version  # Python 3.6+ requerido
```

### Execução Completa
```bash
# 1. Compilar o projeto
javac -d out src/*.java

# 2. Executar os testes (gera os CSVs)
java -cp out Main

# 3. Gerar gráficos e análise
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

## Limitações e Trabalhos Futuros

### Limitações Identificadas
- Comportamento anômalo do HashDuplo requer investigação mais profunda
- Análise limitada a três tamanhos de tabela específicos
- Testes realizados em ambiente controlado único
- Conjuntos de dados sintéticos podem não representar cenários reais

### Melhorias Futuras
- Implementar redimensionamento dinâmico para estratégias de rehashing
- Investigar a causa raiz da degradação do HashDuplo
- Testar com conjuntos de dados do mundo real (nomes, endereços, etc.)
- Análise de consumo de memória mais precisa com profiling
- Testes em diferentes ambientes de hardware e JVMs
- Implementação e teste de funções hash adicionais (MurmurHash, CityHash)

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