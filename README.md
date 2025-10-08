# TrackLift ASA

Um aplicativo Android para gerenciamento de treinos, academias e exercícios desenvolvido em Kotlin com Jetpack Compose.

## 📱 Funcionalidades

- **Gerenciamento de Academias**: Criar, editar e listar academias
- **Catálogo de Exercícios**: Banco de exercícios com categorias
- **Criação de Treinos**: Montar treinos personalizados
- **Cronômetro e Timer**: Ferramentas para controle de tempo nos treinos
- **Perfil do Usuário**: Gerenciamento de dados pessoais
- **Cálculo de IMC**: Calculadora de Índice de Massa Corporal

## 🛠️ Tecnologias Utilizadas

- **Kotlin** - Linguagem de programação
- **Jetpack Compose** - UI moderna e declarativa
- **Room Database** - Banco de dados local
- **Supabase** - Backend e autenticação
- **MVVM Architecture** - Arquitetura de desenvolvimento
- **Navigation Compose** - Navegação entre telas
- **ViewModels** - Gerenciamento de estado da UI

## 📋 Pré-requisitos

- Android Studio Arctic Fox ou superior
- JDK 11 ou superior
- Android SDK API 24 ou superior
- Dispositivo Android ou emulador

## 🚀 Como executar o projeto

### 1. Clone o repositório
```bash
git clone https://github.com/Fabio-MB/TrackLift.git
cd TrackLift
```

### 2. Abra o projeto no Android Studio
- Abra o Android Studio
- Selecione "Open an existing Android Studio project"
- Navegue até a pasta do projeto e selecione-a

### 3. Configuração do Gradle
- O Android Studio irá sincronizar automaticamente as dependências
- Aguarde o processo de sync completar

### 4. Configuração do Supabase (Opcional)
Se você quiser usar as funcionalidades de autenticação:
1. Crie uma conta no [Supabase](https://supabase.com)
2. Crie um novo projeto
3. Configure as credenciais no arquivo de configuração

### 5. Execute o projeto
- Conecte um dispositivo Android ou inicie um emulador
- Clique no botão "Run" (▶️) ou pressione Shift + F10

## 📁 Estrutura do Projeto

```
app/src/main/java/com/example/tracklift_asa/
├── data/                    # Camada de dados
│   ├── entities/           # Entidades do Room
│   ├── daos/              # Data Access Objects
│   └── database/          # Configuração do banco
├── ui/                     # Camada de apresentação
│   ├── screens/           # Telas da aplicação
│   ├── viewmodels/        # ViewModels
│   └── components/        # Componentes reutilizáveis
├── navigation/            # Configuração de navegação
└── utils/                 # Utilitários e helpers
```

## 🎯 Principais Telas

- **HomeScreen**: Tela principal com navegação
- **ListaAcademiasScreen**: Lista de academias cadastradas
- **CrudAcademiaScreen**: Criação e edição de academias
- **ListaExerciciosScreen**: Catálogo de exercícios
- **CrudExercicioScreen**: Criação e edição de exercícios
- **ListaTreinosScreen**: Treinos do usuário
- **CrudTreinoScreen**: Criação e edição de treinos
- **CronometroScreen**: Cronômetro para treinos
- **TimerScreen**: Timer para intervalos
- **ProfileScreen**: Perfil do usuário
- **ImcScreen**: Calculadora de IMC

## 🔧 Configurações Importantes

### Gradle Wrapper
O projeto usa o Gradle Wrapper. Certifique-se de que o arquivo `gradlew.bat` está presente para Windows.

### Dependências Principais
- Compose BOM
- Room Database
- Navigation Compose
- Supabase Android
- Material Design 3

## 📱 Testando o App

1. **Academias**: Teste criando, editando e listando academias
2. **Exercícios**: Adicione exercícios com diferentes categorias
3. **Treinos**: Monte treinos personalizados combinando exercícios
4. **Timer/Cronômetro**: Use as ferramentas de tempo durante os treinos
5. **IMC**: Calcule seu índice de massa corporal

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Desenvolvedor

**Fabio-MB**
- GitHub: [@Fabio-MB](https://github.com/Fabio-MB)

## 🐛 Problemas Conhecidos

- Algumas funcionalidades do Supabase podem requerer configuração adicional
- O arquivo `local.properties` não deve ser commitado (já está no .gitignore)

## 📞 Suporte

Se você encontrar algum problema ou tiver dúvidas, abra uma issue no repositório GitHub.
