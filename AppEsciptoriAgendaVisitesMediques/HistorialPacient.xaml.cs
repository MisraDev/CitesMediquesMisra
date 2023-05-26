using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// La plantilla de elemento Página en blanco está documentada en https://go.microsoft.com/fwlink/?LinkId=234238

namespace AppEsciptoriAgendaVisitesMediques
{
    /// <summary>
    /// Una página vacía que se puede usar de forma independiente o a la que se puede navegar dentro de un objeto Frame.
    /// </summary>
    public sealed partial class HistorialPacient : Page
    {
        String nifPacient;
        private static String nomUsuari = "dam2_mdelaro4";
        private static String contraseya = "6673C";
        private static String path = "Fdam2_mdelaro4/FReports/FHistorialPacients";
        public HistorialPacient()
        {
            this.InitializeComponent();
            


        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            if (e.Parameter != null)
            {
                nifPacient = (string)e.Parameter;

            }
            //nifText.Text = nifPacient;
            webViewJaper.Navigate(new Uri("http://51.68.224.27:8080/jasperserver/flow.html?" +
    "_flowId=viewReportFlow&standAlone=true&" +
    "ParentFolderUri=%2Fdam2_mdelaro4%2FReports&" +
    "reportUnit=%2Fdam2_mdelaro4%2FReports%2FHistorialPacients&" +
    "j_username=" + nomUsuari + "&j_password=" + contraseya + "&NIF=" + nifPacient));

           

            
        }

        private void torna_Click(object sender, RoutedEventArgs e)
        {
            Frame.Navigate(typeof(MainPage));

        }
    }
}
