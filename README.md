# BioFace - Exemplo de Uso no Kotlin

Este projeto é um exemplo de aplicação Android que utiliza um `WebView` para acessar páginas de registro e login do fioface. A aplicação tem dois botões principais: "Registrar" e "Login". O botão "Registrar" carrega uma página de registro diretamente, enquanto o botão "Login" solicita que o usuário insira um `userId` antes de carregar a página de login correspondente.

## Estrutura do Projeto

### Arquivos Principais

- **`MainActivity.kt`**: A atividade principal que gerencia o `WebView` e a lógica dos botões de "Registrar" e "Login".
- **`main_activity.xml`**: Layout da interface do usuário que contém o `WebView`, um `ProgressBar` e dois botões: "Registrar" e "Login".

### Detalhes do Código

#### `MainActivity.kt`

A `MainActivity` é responsável por:

- Inicializar e configurar o `WebView` para carregar as URLs de registro e login.
- Configurar os botões de "Registrar" e "Login".
- Pedir ao usuário que insira o `userId` para efetuar o login.

##### Principais Métodos:

1. **`onCreate`**: Método inicial onde a interface do usuário é configurada e os botões são inicializados.
2. **`startWebViewRegister`**: Carrega a URL de registro no `WebView`.
3. **`promptForUserIdAndLoadUrl`**: Exibe um diálogo para o usuário inserir o `userId` e, se fornecido, carrega a URL de login do bioface no `WebView`.
4. **`setWebViewClient`**: Configura o cliente do `WebView` para gerenciar o progresso do carregamento da página, exibindo ou ocultando o `ProgressBar`.

#### `main_activity.xml`

O arquivo de layout XML define a estrutura visual da aplicação:

- **`WebView`**: Exibe as páginas de registro e login.
- **`ProgressBar`**: Indica o progresso do carregamento da página.
- **`Button`**: Dois botões ("Registrar" e "Login") para interações do usuário.

### Funcionalidades

1. **Registrar**: Ao clicar no botão "Registrar", a URL de registro do BIOFACE é carregada diretamente no `WebView`.
2. **Login**: Ao clicar no botão "Login", o usuário é solicitado a inserir seu `userId`. Se o `userId` for fornecido, a URL de login do BIOFACE correspondente é carregada no `WebView`.

### Como Executar o Projeto

1. Clone o repositório para o seu ambiente local.
2. Abra o projeto no Android Studio.
3. Conecte um dispositivo Android ou use um emulador.
4. Clique em "Run" para compilar e executar a aplicação.


### Dependências

Este projeto não possui dependências externas específicas além das bibliotecas padrão do Android SDK.


## Contato

Para dúvidas ou sugestões, entre em contato com o desenvolvedor: [jeova.junior@me2.com.br](mailto:jeova.junior@me2.com.br).
