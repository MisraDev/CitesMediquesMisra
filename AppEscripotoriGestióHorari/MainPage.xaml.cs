using AppEscripotoriGestióHorari.model;
using DbLibrary.modelo;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
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

namespace AppEscripotoriGestióHorari
{
    /// <summary>
    /// Página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class MainPage : Page
    {
        private int mCodiMetge = 0;
        private List<Hora> horari;
        List<FilaDataGrid> oldDades;
        List<FilaDataGrid> dades;
        ObservableCollection<AgendaMetgeDB> agendaMetge;
        public ObservableCollection<EspecialitatDB> Especialitats
        {
            get { return (ObservableCollection<EspecialitatDB>)GetValue(EspecialitatsProperty); }
            set { SetValue(EspecialitatsProperty, value); }
        }

        public static readonly DependencyProperty EspecialitatsProperty =
            DependencyProperty.Register("Especialitats", typeof(ObservableCollection<EspecialitatDB>), typeof(MainPage), new PropertyMetadata(new List<EspecialitatDB>()));
        public MainPage()
        {
            this.InitializeComponent();
            btnClick.IsEnabled = false;
            PersonaDB persona = PersonaDB.getPersonasDB(1);
            Debug.WriteLine("persona" + persona.Nom);

            ObservableCollection<EspecialitatDB> citass = new ObservableCollection<EspecialitatDB>(EspecialitatDB.getEspecialitatsDBPerMetge(4));
            
            foreach (EspecialitatDB cita in citass)
            {
                Debug.WriteLine("Esp: " + cita.Codi + " - " + cita.NomEspecialitat);
            }
            codiMetge.TextChanged += codiMetge_TextChanged;
        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private async void codiMetge_TextChanged(object sender, TextChangedEventArgs e)
        {
            if (int.TryParse(codiMetge.Text, out int codigoMetge))
            {
                // El texto ingresado es un número válido
                mCodiMetge = codigoMetge;
                horari = Hora.GenerarHorari();
                agendaMetge = AgendaMetgeDB.GetHorari(mCodiMetge);
                Especialitats = EspecialitatDB.getEspecialitatsDBPerMetge(mCodiMetge);
                dades = new List<FilaDataGrid>();
                dades = Utils.GenerarLlistaEspecialitats(horari, dades, Especialitats, agendaMetge);
                HorariDataGrid.ItemsSource = dades;
                oldDades = Utils.OmplirLLista(dades);

                btnClick.IsEnabled = false;
            }
            else
            {
                // El texto ingresado no es un número válido
                // Muestra un mensaje de error
                var dialog = new MessageDialog("Por favor, introduce un número entero válido.");
                await dialog.ShowAsync();
            }

        }
        private void HorariDataGrid_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            btnClick.IsEnabled = true;

        }

        private void Desar_Click(object sender, RoutedEventArgs e)
        {
            List<FilaDataGrid> newDades = (List<FilaDataGrid>)HorariDataGrid.ItemsSource;
            Utils.DesarHorariMetge( oldDades, newDades, mCodiMetge);
            infotText.Text = "Dades desades!";

        }
    }
}
