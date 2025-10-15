import os
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# ========================
# CONFIGURAÇÕES GLOBAIS
# ========================

plt.style.use("seaborn-v0_8-muted")
plt.rcParams.update({
    "figure.dpi": 120,
    "axes.titlesize": 13,
    "axes.labelsize": 11,
    "legend.fontsize": 9,
    "axes.facecolor": "#fafafa",
    "axes.edgecolor": "#dddddd",
    "axes.grid": True,
    "grid.alpha": 0.5,
    "grid.linestyle": ":",
})

# Diretórios
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PLOT_DIR = os.path.join(BASE_DIR, "plots")
os.makedirs(PLOT_DIR, exist_ok=True)

# Paleta de cores
COLOR_MAP = {
    "HashRehashing": "#1f77b4",      # azul
    "HashEncadeamento": "#d62728",   # vermelho
    "HashDuplo": "#2ca02c",          # verde
}

# Dicionário para armazenar dados agregados
comparativo_data = {
    "colisoes": {},
    "tempoInsercao": {},
    "tempoBusca": {},
    "maiorGap": {},
    "menorGap": {},
    "mediaGap": {}
}

# Métricas que devem usar escala log
LOG_METRICS = {"colisoes", "tempoInsercao", "tempoBusca"}


# ========================
# FUNÇÕES AUXILIARES
# ========================

def plot_metric_for_table(table_size: str, metric_name: str, df: pd.DataFrame):
    """Gera gráfico de barras agrupadas por função hash para uma tabela específica."""
    plt.figure(figsize=(9, 5))

    # Eixo X
    x_labels = sorted(df["tamanhoConjuntoDados"].unique())
    x = np.arange(len(x_labels))
    width = 0.25

    funcs = list(df["funcaoHash"].unique())
    offsets = np.linspace(-width, width, len(funcs))

    y_max_global = 0

    for i, funcao in enumerate(funcs):
        subset = df[df["funcaoHash"] == funcao].sort_values("tamanhoConjuntoDados")
        color = COLOR_MAP.get(funcao, None)
        plt.bar(
            x + offsets[i],
            subset[metric_name],
            width=width,
            label=funcao,
            color=color,
            edgecolor="black",
            linewidth=0.4,
        )

        y_max_global = max(y_max_global, subset[metric_name].max())

        # Anotação de valores nas barras (somente se houver poucos pontos)
        if len(x_labels) <= 10:
            for xi, yi in zip(x + offsets[i], subset[metric_name]):
                plt.text(xi, yi * 1.1, f"{yi:.2f}", ha="center", va="bottom", fontsize=7)

        # Salva para comparativo
        comparativo_data[metric_name].setdefault(table_size, []).append({
            "funcao": funcao,
            "x": x_labels,
            "y": subset[metric_name].tolist()
        })

    # Escala log apenas se estiver na lista
    if metric_name in LOG_METRICS:
        plt.yscale("log")

    plt.title(f"{metric_name.capitalize()} - Tabela Hash: {table_size}")
    plt.xlabel("Tamanho do Conjunto de Dados")
    plt.ylabel(metric_name.capitalize())
    plt.xticks(x, x_labels, rotation=30)
    plt.legend(title="Função Hash", loc="best")
    plt.tight_layout()

    output_file = os.path.join(PLOT_DIR, f"{table_size}_{metric_name}.png")
    plt.savefig(output_file)
    plt.close()
    print(f"✅ Gráfico salvo em: {output_file}")


def plot_comparativo_final():
    """Gera gráfico comparativo global (barras agrupadas por função e tamanho de tabela)."""
    for metric_name, tabelas in comparativo_data.items():
        if not tabelas:
            continue

        plt.figure(figsize=(10, 6))
        all_data = []

        for table_size, series_list in tabelas.items():
            for serie in series_list:
                for xi, yi in zip(serie["x"], serie["y"]):
                    all_data.append({
                        "tabela": int(table_size),
                        "tamanhoConjunto": xi,
                        "funcao": serie["funcao"],
                        metric_name: yi
                    })

        df_all = pd.DataFrame(all_data)
        df_grouped = df_all.groupby(["tabela", "funcao"])[metric_name].mean().reset_index()

        x_labels = sorted(df_grouped["tabela"].unique())
        x = np.arange(len(x_labels))
        funcs = df_grouped["funcao"].unique()
        width = 0.25
        offsets = np.linspace(-width, width, len(funcs))

        for i, funcao in enumerate(funcs):
            subset = df_grouped[df_grouped["funcao"] == funcao]
            color = COLOR_MAP.get(funcao, None)
            plt.bar(
                x + offsets[i],
                subset[metric_name],
                width=width,
                label=funcao,
                color=color,
                edgecolor="black",
                linewidth=0.4,
            )
            for xi, yi in zip(x + offsets[i], subset[metric_name]):
                plt.text(xi, yi * 1.1, f"{yi:.2f}", ha="center", va="bottom", fontsize=7)

        # Escala log apenas nas métricas relevantes
        if metric_name in LOG_METRICS:
            plt.yscale("log")

        plt.title(f"Comparativo Global - Média de {metric_name.capitalize()}")
        plt.xlabel("Tamanho da Tabela Hash")
        plt.ylabel(metric_name.capitalize())
        plt.xticks(x, x_labels)
        plt.legend(title="Função Hash", loc="best")
        plt.tight_layout()

        output_file = os.path.join(PLOT_DIR, f"comparativo_{metric_name}.png")
        plt.savefig(output_file)
        plt.close()
        print(f"📊 Gráfico comparativo salvo em: {output_file}")


# ========================
# FUNÇÃO PRINCIPAL
# ========================

def main():
    dirnames = [d for d in os.listdir(BASE_DIR) if os.path.isdir(os.path.join(BASE_DIR, d)) and d.isdigit()]
    dirnames.sort(key=int)

    for dirname in dirnames:
        dirpath = os.path.join(BASE_DIR, dirname)
        print(f"\n📊 Processando tabela hash: {dirname}")

        for metric in ["colisoes", "tempoInsercao", "tempoBusca", "maiorGap", "menorGap", "mediaGap"]:
            filepath = os.path.join(dirpath, f"{metric}.csv")
            if os.path.exists(filepath):
                df = pd.read_csv(filepath)
                plot_metric_for_table(dirname, metric, df)
            else:
                print(f"⚠️ Arquivo não encontrado: {filepath}")

    plot_comparativo_final()


if __name__ == "__main__":
    main()