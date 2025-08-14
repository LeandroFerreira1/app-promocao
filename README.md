# 🛒 Guia das Ofertas - App de Promoções

Um aplicativo Android nativo desenvolvido em Java para conectar consumidores e estabelecimentos através de promoções e ofertas em tempo real.

## 📱 Sobre o Projeto

O **Guia das Ofertas** é uma plataforma mobile que permite aos usuários descobrir, compartilhar e avaliar promoções de estabelecimentos, com foco especial em supermercados e comércios locais. O app combina geolocalização, gamificação e interação social para criar uma experiência única de economia colaborativa.

## ✨ Principais Funcionalidades

### 🎯 Para Consumidores
- **Mapa Interativo**: Visualize promoções próximas à sua localização usando Google Maps
- **Feed de Promoções**: Timeline com as melhores ofertas da sua região
- **Sistema de Avaliações**: Avalie e comente promoções compartilhadas por outros usuários
- **Ranking e Gamificação**: Sistema de pontuação e conquistas para usuários ativos
- **Perfil Personalizado**: Gerencie suas promoções favoritas e histórico
- **Scanner de Código de Barras**: Identifique produtos rapidamente

### 🏪 Para Estabelecimentos
- **Cadastro de Promoções**: Crie e gerencie ofertas com fotos, preços e validade
- **Gestão de Produtos**: Cadastre produtos com código de barras
- **Perfil do Estabelecimento**: Página dedicada com informações e avaliações
- **Localização no Mapa**: Apareça no mapa para clientes próximos

### 🎮 Sistema de Gamificação
- **Sistema de Pontuação**: Ganhe pontos compartilhando promoções válidas
- **Conquistas**: Desbloqueie badges por atividades no app
- **Ranking de Usuários**: Compete com outros usuários da comunidade

## 🛠️ Tecnologias Utilizadas

### Core
- **Java** - Linguagem principal
- **Android SDK** (API 21-32) - Plataforma de desenvolvimento
- **Gradle** - Sistema de build

### APIs e Serviços
- **Google Maps API** - Mapas e geolocalização
- **Google Play Services** - Serviços do Google
- **Retrofit 2** - Cliente HTTP para APIs REST
- **Gson** - Serialização/deserialização JSON

### Interface e UX
- **Material Design Components** - Design system do Google
- **ConstraintLayout** - Layouts responsivos
- **ViewPager** - Navegação por abas
- **SmartTabLayout** - Tabs customizadas
- **CircleImageView** - Imagens circulares para perfis

### Recursos Avançados
- **Picasso** - Carregamento e cache de imagens
- **uCrop** - Editor de imagens integrado
- **Code Scanner** - Leitor de código de barras
- **FileProvider** - Compartilhamento seguro de arquivos

## 📋 Pré-requisitos

- Android Studio 4.0+
- JDK 8+
- Android SDK (API 21 ou superior)
- Google Maps API Key
- Dispositivo/Emulador Android 5.0+ (API 21)

## 🚀 Como Executar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/app-promocao.git
   cd app-promocao
   ```

2. **Configure a API Key do Google Maps**
   - Obtenha uma API Key no [Google Cloud Console](https://console.cloud.google.com/)
   - Adicione a chave no arquivo `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="MAPS_API_KEY">SUA_API_KEY_AQUI</string>
   ```

3. **Abra no Android Studio**
   - File → Open → Selecione a pasta do projeto

4. **Sincronize as dependências**
   - O Gradle irá baixar automaticamente todas as dependências

5. **Execute o projeto**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em "Run" ou use `Shift + F10`


## 🏗️ Arquitetura do Projeto

```
app/
├── src/main/java/br/com/mexy/promo/
│   ├── activity/          # Activities (telas do app)
│   ├── adapter/           # Adapters para RecyclerViews
│   ├── api/              # Serviços de API (Retrofit)
│   ├── fragment/         # Fragments e Bottom Sheets
│   ├── model/            # Classes de modelo/entidade
│   └── util/             # Classes utilitárias
├── src/main/res/
│   ├── layout/           # Layouts XML
│   ├── drawable/         # Recursos gráficos
│   ├── values/           # Strings, cores, estilos
│   └── menu/             # Menus da aplicação
└── AndroidManifest.xml   # Configurações do app
```

## 🔐 Permissões Necessárias

- **INTERNET** - Comunicação com APIs
- **ACCESS_FINE_LOCATION** - Localização precisa
- **ACCESS_COARSE_LOCATION** - Localização aproximada
- **CAMERA** - Scanner de código de barras
- **CALL_PHONE** - Ligações para estabelecimentos
- **READ/WRITE_EXTERNAL_STORAGE** - Gerenciamento de imagens

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

*Transformando a forma como as pessoas descobrem e compartilham ofertas! 🛍️*
