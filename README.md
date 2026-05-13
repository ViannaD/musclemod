# 💪 Muscle Mod — Minecraft 1.20.1 Forge

Adiciona os comandos `/muscle` e `/normal` para alternar entre o corpo musculoso e o corpo normal do jogador.  
**Funciona com qualquer skin** — a textura do jogador é aplicada automaticamente sobre o modelo muscle.

---

## 📦 Dependências

| Dependência | Versão | Link |
|-------------|--------|------|
| Minecraft Forge | 1.20.1-47.2.0 | https://files.minecraftforge.net |
| GeckoLib | 4.3.1 for 1.20.1 | https://www.curseforge.com/minecraft/mc-mods/geckolib |

---

## 🚀 Como compilar

### Pré-requisitos
- Java 17 (JDK)
- Internet (Gradle baixa as dependências)

### Passos

```bash
# 1. Abra a pasta do mod no terminal
cd musclemod

# 2. Rode o setup do Forge (baixa o Minecraft deobfuscado)
./gradlew genEclipseRuns   # ou genIntellijRuns

# 3. Compile o mod
./gradlew build

# 4. O .jar compilado estará em:
#    build/libs/musclemod-1.0.0.jar
```

No **Windows** use `gradlew.bat` no lugar de `./gradlew`.

---

## 📁 Instalação

1. Instale o **Minecraft Forge 1.20.1** (versão 47.2.0)
2. Baixe o **GeckoLib 4.3.1** para 1.20.1
3. Coloque ambos `.jar` na pasta `mods/`
4. Coloque o `musclemod-1.0.0.jar` também na pasta `mods/`
5. Inicie o jogo!

---

## 🎮 Comandos

| Comando | Efeito |
|---------|--------|
| `/muscle` | Ativa o corpo musculoso 💪 |
| `/normal` | Volta ao corpo normal |

- O estado é sincronizado para todos os jogadores próximos (multiplayer)
- O estado é salvo ao trocar de dimensão / morrer / reconectar

---

## 🏗️ Estrutura do Projeto

```
musclemod/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── src/main/
    ├── java/com/musclemod/
    │   ├── MuscleMod.java              ← Classe principal
    │   ├── command/
    │   │   └── MuscleCommand.java      ← Comandos /muscle e /normal
    │   ├── common/
    │   │   ├── MuscleCapability.java   ← Armazena o estado muscle
    │   │   ├── MuscleCapabilityProvider.java  ← Registra nos players
    │   │   └── CapabilityRegistration.java
    │   ├── network/
    │   │   ├── ModNetwork.java         ← Sincronização servidor→cliente
    │   │   └── ClientPacketHandler.java
    │   └── client/
    │       ├── ClientSetup.java        ← Injeta o render layer
    │       ├── PlayerRenderEvents.java ← Cancela render vanilla quando muscle
    │       ├── MuscleModelRenderer.java ← Desenha o modelo musculoso
    │       ├── MuscleRenderLayer.java  ← Layer adicional no PlayerRenderer
    │       └── MusclePlayerModel.java  ← Modelo GeckoLib (backup)
    └── resources/
        ├── META-INF/
        │   ├── mods.toml
        │   └── accesstransformer.cfg
        ├── pack.mcmeta
        └── assets/musclemod/
            ├── geo/
            │   └── muscle_player.geo.json   ← Geometria do modelo
            └── animations/
                └── muscle_player.animation.json
```

---

## ⚙️ Como funciona

1. **Servidor**: `/muscle` salva o estado na `Capability` do jogador e envia um pacote de rede para todos os clientes próximos.
2. **Cliente**: Ao receber o pacote, atualiza o estado local. O `PlayerRenderEvents` cancela o render vanilla e chama o `MuscleModelRenderer` diretamente, aplicando a skin real do jogador sobre o modelo musculoso.
3. O modelo usa as mesmas coordenadas UV do arquivo `muscle_geo.json` original do Blockbench, então a skin padrão 64×64 encaixa perfeitamente.

---

## 📝 Notas de desenvolvimento

- O modelo NÃO usa GeckoLib em runtime — o `MuscleModelRenderer` usa a API nativa `ModelPart` do Minecraft, o que é mais leve e não depende de animações externas.
- O arquivo GeckoLib `.geo.json` está incluído caso queira usar animações no futuro.
- Para adicionar animações (andar, correr, atacar) basta popular o `muscle_player.animation.json` e usar a classe `MusclePlayerModel`.
