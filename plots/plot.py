import os
import pandas as pd
import matplotlib.pyplot as plt

# Diretórios
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
PLOT_DIR = os.path.join(BASE_DIR, "plots")
os.makedirs(PLOT_DIR, exist_ok=True)

def plot_metric_for_table(table_size: str, metric_name: str, df: pd.DataFrame):
    """Gera e salva um gráfico para uma métrica em uma tabela específica."""
    plt.figure(figsize=(8, 5))

    for funcao, grupo in df.groupby("funcaoHash"):
        plt.plot(
            grupo["tamanhoConjuntoDados"],
            grupo[metric_name],
            marker="o",
            label=funcao
        )

    plt.title(f"{metric_name.capitalize()} - Tabela Hash: {table_size}")
    plt.xlabel("Tamanho do Conjunto de Dados")
    plt.ylabel(metric_name.capitalize())
    plt.xscale("log")
    plt.yscale("log")
    plt.grid(True, which="both", ls="--", lw=0.5)
    plt.legend(title="Função Hash", fontsize=8)
    plt.tight_layout()

    output_file = os.path.join(PLOT_DIR, f"{table_size}_{metric_name}.png")
    plt.savefig(output_file)
    plt.close()
    print(f"✅ Gráfico salvo em: {output_file}")

def main():
    for dirname in os.listdir(BASE_DIR):
        dirpath = os.path.join(BASE_DIR, dirname)
        if os.path.isdir(dirpath) and dirname.isdigit():
            print(f"\n📊 Processando tabela hash: {dirname}")

            for metric in ["colisoes", "tempo"]:
                filepath = os.path.join(dirpath, f"{metric}.csv")
                if os.path.exists(filepath):
                    df = pd.read_csv(filepath)
                    plot_metric_for_table(dirname, metric, df)
                else:
                    print(f"⚠️ Arquivo não encontrado: {filepath}")

if __name__ == "__main__":
    main()
