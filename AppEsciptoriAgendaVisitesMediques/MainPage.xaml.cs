using AppEsciptoriAgendaVisitesMediques.model;
using DbLib.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Popups;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=402352&clcid=0xc0a

namespace AppEsciptoriAgendaVisitesMediques
{
    /// <summary>
    /// Página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        private List<string> encabezados;
        List<Hora> horari;
        private int mCodiMetge = 0;
        private int mCodiPacient;
        public MainPage()
        {
            this.InitializeComponent();
            //DataContext = new MyViewModel();
            InformeText.IsEnabled = false;

            ObservableCollection<CitaDB> citass = new ObservableCollection<CitaDB>(CitaDB.getCitasDB(4));
            PersonaDB p = PersonaDB.getPersonasDB(1);
            foreach (CitaDB cita in citass)
            {
                Debug.WriteLine("Cita: " + cita.Persona.Codi + " - " + cita.DataCita + " - " + cita.Informe);
            }
            
            Debug.WriteLine("persona" + p.Nom);
            //PersonaDB

            CodiMetgeText.TextChanged += CodiMetgeText_TextChanged;
        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {
            
        }

        private async void CodiMetgeText_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (int.TryParse(CodiMetgeText.Text, out int codigoMetge))
            {
                // El texto ingresado es un número válido
                mCodiMetge = codigoMetge;
                horari = Hora.GenerarHorari();
                ObservableCollection<CitaDB> cites = new ObservableCollection<CitaDB>();
                cites = CitaDB.getCitasDBSemanaActual(codigoMetge);

                String setmana = "actual";
                Utils.GenerarDataGrid(dtgAgenda, cites, horari, setmana);
                PrevBtn.IsEnabled = true;
                ActualBtn.IsEnabled = false;
                NextBtn.IsEnabled = true;
            }
            else
            {
                // El texto ingresado no es un número válido
                // Muestra un mensaje de error
                var dialog = new MessageDialog("Por favor, introduce un número entero válido.");
                await dialog.ShowAsync();
            }
        }

        private void Actual_Click(object sender, RoutedEventArgs e)
        {
            horari = Hora.GenerarHorari();
            ObservableCollection<CitaDB> cites = new ObservableCollection<CitaDB>();
            cites = CitaDB.getCitasDBSemanaActual(mCodiMetge);

            String setmana = "actual";
            Utils.GenerarDataGrid(dtgAgenda, cites, horari, setmana);
            PrevBtn.IsEnabled = true;
            ActualBtn.IsEnabled = false;
            NextBtn.IsEnabled = true;
        }

        private void Previ_Click(object sender, RoutedEventArgs e)
        {
            horari = Hora.GenerarHorari();
            ObservableCollection<CitaDB> cites = new ObservableCollection<CitaDB>();
            cites = CitaDB.getCitasDBSemanaAnterior(mCodiMetge);

            String setmana = "previ";
            Utils.GenerarDataGrid(dtgAgenda, cites, horari, setmana);
            PrevBtn.IsEnabled = false;
            ActualBtn.IsEnabled = true;
            NextBtn.IsEnabled = true;
        }

        private void Seguent_Click(object sender, RoutedEventArgs e)
        {
            horari = Hora.GenerarHorari();
            ObservableCollection<CitaDB> cites = new ObservableCollection<CitaDB>();
            cites = CitaDB.getCitasDBSemanaSiguiente(mCodiMetge);

            String setmana = "seguent";
            Utils.GenerarDataGrid(dtgAgenda, cites, horari, setmana);
            PrevBtn.IsEnabled = true;
            ActualBtn.IsEnabled = true;
            NextBtn.IsEnabled = false;
        }

        private void dtgAgenda_SelectionChanged(Object sender, SelectionChangedEventArgs e)
        {
            CodiPacientText.Text = "";
            int objecteSeleccionat = 0;
            DataRow citaSeleccionada = (DataRow)dtgAgenda.SelectedItem;
            if (citaSeleccionada != null)
            {
                objecteSeleccionat = dtgAgenda.CurrentColumn.DisplayIndex;
            }



            if(citaSeleccionada != null && objecteSeleccionat>0)
            {
                string valorColumna = citaSeleccionada[objecteSeleccionat].ToString();

                if (!string.IsNullOrEmpty(valorColumna) && valorColumna[0] == 'C')
                {
                    
                    InformeText.IsEnabled = true;
                    HistorialBtn.IsEnabled = true;
                    CitaDB cita = new CitaDB();

                    //CitaDB() citaSeleccionada[objecteSeleccionat].

                    foreach (Hora hora in horari)
                    {
                        if (hora.HoraCol.Equals(citaSeleccionada[0]))
                        {
                            switch (objecteSeleccionat)
                            {
                                case 1:
                                    cita = hora.Dilluns;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;
                                case 2:
                                    cita = hora.Dimarts;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;
                                case 3:
                                    cita = hora.Dimecres;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;
                                case 4:
                                    cita = hora.Dijous;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;
                                case 5:
                                    cita = hora.Divendres;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;
                                case 6:
                                    cita = hora.Dissabte;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;
                                case 7:
                                    cita = hora.Diumenge;
                                    mCodiPacient = cita.Persona.Codi;
                                    break;

                            }
                        }
                    }
                    //String nomIDataNaix = PersonaDB.getPersonasDB(Cita.Persona.Codi);
                    PersonaDB p = PersonaDB.getPersonasDB(cita.Persona.Codi);
                    PacientText.Text = p.Nom;
                    InformeText.Text = cita.Informe != null ? cita.Informe : "";
                    CodiPacientText.Text = "Codi Pacient " + mCodiPacient;
                }
                
            }
            else
            {
                PacientText.Text = "";
                EdatText.Text = "";
                InformeText.Text = "";
                DesatText.Text = "";
            }
        }

        private void InformeText_TextChanged(object sender, TextChangedEventArgs e)
        {
            DesarBtn.IsEnabled = !string.IsNullOrEmpty(InformeText.Text);
            DesatText.Text = "";
        }

        private void Desar_Click(object sender, RoutedEventArgs e)
        {
            int objecteSeleccionat = 0;
            DataRow citaSeleccionada = (DataRow)dtgAgenda.SelectedItem;
            if (citaSeleccionada != null)
            {
                objecteSeleccionat = dtgAgenda.CurrentColumn.DisplayIndex;
            }

            if (citaSeleccionada != null && objecteSeleccionat > 0 )
            {
                string valorColumna = citaSeleccionada[objecteSeleccionat].ToString();
                if (!string.IsNullOrEmpty(valorColumna) && valorColumna[0] == 'C')
                {
                    CitaDB cita = new CitaDB();

                    foreach (Hora hora in horari)
                    {
                        if (hora.HoraCol.Equals(citaSeleccionada[0]))
                        {
                            switch (objecteSeleccionat)
                            {
                                case 1:
                                    cita = hora.Dilluns;
                                    break;
                                case 2:
                                    cita = hora.Dimarts;
                                    break;
                                case 3:
                                    cita = hora.Dimecres;
                                    break;
                                case 4:
                                    cita = hora.Dijous;
                                    break;
                                case 5:
                                    cita = hora.Divendres;
                                    break;
                                case 6:
                                    cita = hora.Dissabte;
                                    break;
                                case 7:
                                    cita = hora.Diumenge;
                                    break;

                            }
                        }
                    }
                    
                    
                    if (cita.Informe != null && cita.Informe.Equals(InformeText.Text))
                    {
                        DesatText.Text = "Informe ja desat";
                    } else if((cita.Informe == null || !cita.Informe.Equals(InformeText.Text)) && Utils.Ultimes48Horas(cita.DataCita))
                    {
                        cita.Informe = InformeText.Text;
                        if (CitaDB.updateInformeCita(cita))
                        {
                            DesatText.Text = "Informe Desat correctament";
                        }

                    }
                    
                    if (!Utils.Ultimes48Horas(cita.DataCita))
                    {
                        DesatText.Text = "L'informe no es pot editar, han passat 48 hores";
                    }

                }
                
            }
            
        }

        private void Historial_Click(object sender, RoutedEventArgs e)
        {
            String nif = "";
            PersonaDB persona = PersonaDB.getPersonasDB(mCodiPacient);
            if (persona != null)
            {
                Frame rootFrame = Window.Current.Content as Frame;
                rootFrame.Navigate(typeof(HistorialPacient), persona.Nif);

            }
            else
            {
                //var dialog = new MessageDialog("No has seleccionado una cita que contenga persona con nif.");
                Debug.WriteLine("No ha seleccionado una cita con persona");

            }
             
            

        }







    }
}
