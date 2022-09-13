# rappi-marvel

<H1>MVVM Clean Architecture</H1>

<H2>Capas de la arquitectura</H2>

![Android MVVM Clean Achitecture](https://cursokotlin.com/wp-content/uploads/2021/05/Android-Clean-Architecture.png)

<ol>
  <li>
    <p>Presentación (app)</p>
    <ul>
      <li><p>UI en general (Fragmentos, Activities, Adapters, etc.): Se encargan de mostrar las vistas y navegación al usuario</p></li>
      <li><p>ViewModels: Realizan la comunicación con los casos de uso correspondientes, aqui se aplica MVI</p></li>
      <li><p>States, Events: Forman Parte de la arquitectura MVI para sellar contratos de llamadas</p></li>
      <li><p>Module: Inyección de dependencias de los casos de uso a usar en ViewModel</p></li>
    </ul>
  </li>
  <li>
    <p>Usecases</p>
    <ul>
      <li><p>Contiene la separación de las funcionalidades disponibles (Principio de responsabilidad unica)</p></li>
    </ul>
  </li>
  <li>
    <p>Domain</p>
    <ul>
      <li><p>Contiene los modelos de negocio y los modelos a transferir a las capas superiores</p></li>
    </ul>
  </li>
  <li>
    <p>Data</p>
    <ul>
      <li><p>DataSources: Contiene las definiciones de los contratos de las fuentes de datos disponibles</p></li>
      <li>
        <p>
          Repositories: Son los encargados de validar cuando es apropiado usar cual data source o bien el mezclado de datos que los datasources proveen
        </p>        
      </li>
    </ul>
  </li>
  <li>
    <p>Framework (app)</p>
    <ul>
      <li><p>DataSources: Contiene las implementaciones de las fuentes de datos disponibles (Pueden ser remotos o locales, depende la abstracción)</p></li>
      <li><p>Module: Inyección de dependencias del repositorie de Data, y los DataSources a utilizar</p></li>
    </ul>
  </li>
<ol>
<p>
<H2>PD:</H2><p>Internamente cada clase esta documentada</p> 
  
