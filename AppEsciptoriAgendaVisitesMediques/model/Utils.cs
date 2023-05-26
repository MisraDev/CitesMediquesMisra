using DbLib.Model;
using Microsoft.Toolkit.Uwp.UI.Controls;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Data;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Media;

namespace AppEsciptoriAgendaVisitesMediques.model
{
    class Utils
    {

        public static void GenerarDataGrid(DataGrid agendaDataGrid, ObservableCollection<CitaDB> cites, List<Hora> horari, string setmana)
        {
            //Netejar el grid
            agendaDataGrid.Columns.Clear();

            // Crear un nou objecte DataTable
            DataTable dt = new DataTable();

            //Afegir la capçelera de les columnes
            List<string> dies = CrearColumnasAmbData(dt, setmana);

            //Afegir les cites al horari
            AfegirCites(cites, horari, setmana);

            // Afegir les files amb les cites
            foreach (var hor in horari)
            {
                DataRow row = dt.NewRow();
                row["Hora"] = hor.HoraCol;
                if (hor.Dilluns != null) { row[dies[0]] = hor.Dilluns; }
                if (hor.Dimarts != null) { row[dies[1]] = hor.Dimarts; }
                if (hor.Dimecres != null) { row[dies[2]] = hor.Dimecres; }
                if (hor.Dijous != null) { row[dies[3]] = hor.Dijous; }
                if (hor.Divendres != null) { row[dies[4]] = hor.Divendres; }
                if (hor.Dissabte != null) { row[dies[5]] = hor.Dissabte; }
                if (hor.Diumenge != null) { row[dies[6]] = hor.Diumenge; }
                dt.Rows.Add(row);
            }
            

            //Crear les columnes
            for (int i = 0; i < dt.Columns.Count; i++)
            {
                var column = new DataGridTextColumn()
                {
                    Header = dt.Columns[i].ColumnName,
                    Binding = new Binding { Path = new PropertyPath("[" + i.ToString() + "]") }
                };
                var style2 = new Style(typeof(TextBlock));
                style2.Setters.Add(new Setter(TextBlock.MarginProperty, new Thickness(8, 0, 8, 0)));
                column.ElementStyle = style2; 
                agendaDataGrid.Columns.Add(column);
            }

            //Ficar-ho tot al datagrid
            var rows = new ObservableCollection<object>();
            foreach (DataRow row in dt.Rows)
            {
                rows.Add(row);
            }

            agendaDataGrid.ItemsSource = rows;

            //Alternar color de les files
            Brush lightGray = new SolidColorBrush(Colors.LightBlue);
            agendaDataGrid.AlternatingRowBackground = lightGray;
            agendaDataGrid.GridLinesVisibility = DataGridGridLinesVisibility.All;

            for (int i = 1; i < 8; i++)
            {
                // Centrar text columnes
                var column = agendaDataGrid.Columns[i] as DataGridTextColumn;
                var style = new Style(typeof(TextBlock));
                style.Setters.Add(new Setter(TextBlock.PaddingProperty, new Thickness(8, 0, 8, 0)));
                style.Setters.Add(new Setter(TextBlock.TextAlignmentProperty, TextAlignment.Center));
                
                column.ElementStyle = style;
            }

            if (setmana.Equals("actual")) { pintarColumnaAvui(agendaDataGrid); }
        }


        private static void AfegirCites(ObservableCollection<CitaDB> cites, List<Hora> horari, string setmana)
        {
            foreach (CitaDB cita in cites)
            {
                string horaString = cita.DataCita.ToString("HH:mm");
                foreach (Hora hor in horari)
                {
                    if (hor.HoraCol.Equals(horaString))
                    {
                        switch (cita.DataCita.DayOfWeek)
                        {
                            case DayOfWeek.Monday:
                                hor.Dilluns = cita;
                                break;
                            case DayOfWeek.Tuesday:
                                hor.Dimarts = cita;
                                break;
                            case DayOfWeek.Wednesday:
                                hor.Dimecres = cita;
                                break;
                            case DayOfWeek.Thursday:
                                hor.Dijous = cita;
                                break;
                            case DayOfWeek.Friday:
                                hor.Divendres = cita;
                                break;
                            case DayOfWeek.Saturday:
                                hor.Dissabte = cita;
                                break;
                            case DayOfWeek.Sunday:
                                hor.Diumenge = cita;
                                break;
                        }
                    }
                }
            }
        }

        public static void pintarColumnaAvui(DataGrid gestorInformesDataGrid)
        {
            //Obtenim el nom de la columna
            DateTime dataActual = DateTime.Today;
            string dataFormatada = dataActual.ToString("dd/MM/yyyy");
            string nomDelDia = dataActual.ToString("dddd", new CultureInfo("ca-ES"));
            nomDelDia = char.ToUpper(nomDelDia[0]) + nomDelDia.Substring(1);
            string nomColAvui = nomDelDia + "\n" + dataFormatada;

            // Obtenim la columna corresponent
            var colAvui = gestorInformesDataGrid.Columns.FirstOrDefault(c => c.Header.ToString() == nomColAvui);

            // Verifiquem si la columna existeix
            if (colAvui != null)
            {
                //Estil de cela diferent
                var estil = new Style(typeof(DataGridCell));
                estil.Setters.Add(new Setter(DataGridCell.BackgroundProperty, new SolidColorBrush(Colors.AliceBlue)));

                // Afegim el nou estil a la columna
                colAvui.CellStyle = estil;
            }
        }

        public static List<string> CrearColumnasAmbData(DataTable dt, string setmana)
        {
            // Agregar la primera columna con el encabezado "Hora"
            dt.Columns.Add("Hora", typeof(string));

            List<string> dies = new List<string>();

            switch (setmana)
            {
                case "actual":
                    dies = ObtenirDatesDeLaSetmanaActual();
                    break;
                case "previ":
                    dies = ObtenirDatesDeLaSetmanaAnterior();
                    break;
                case "seguent":
                    dies = ObtenirDatesDeLaSetmanaSeguent();
                    break;
            }

            // Agregar las columnas restantes con los nombres de los días
            foreach (string dia in dies)
            {
                dt.Columns.Add(dia, typeof(string));
            }
            return dies;
        }

        public static List<string> ObtenirDatesDeLaSetmanaActual()
        {
            List<string> dates = new List<string>();

            // Obtenim la data actual y el dia de la setmana actual
            DateTime dataActual = DateTime.Today;
            DayOfWeek diaActual = dataActual.DayOfWeek;

            // Calculem el primer dia de la setmana
            DateTime primerDiaDeLaSetmana = dataActual.AddDays((-(int)diaActual) + 1);

            // Afegim les dates de cada dia de la setmana actual a la llista
            for (int i = 0; i < 7; i++)
            {
                DateTime data = primerDiaDeLaSetmana.AddDays(i);
                string dataFormatada = data.ToString("dd/MM/yyyy");
                string nomDelDia = data.ToString("dddd", new CultureInfo("ca-ES"));
                nomDelDia = char.ToUpper(nomDelDia[0]) + nomDelDia.Substring(1);
                dates.Add(nomDelDia + "\n" + dataFormatada);
            }

            return dates;
        }

        public static List<string> ObtenirDatesDeLaSetmanaAnterior()
        {
            List<string> dates = new List<string>();

            // Obtenim la data actual y el dia de la setmana actual
            DateTime dataActual = DateTime.Today;
            DayOfWeek diaActual = dataActual.DayOfWeek;

            // Calculem el primer dia de la setmana anterior
            DateTime primerDiaDeLaSetmanaAnterior = dataActual.AddDays(-(int)diaActual - 6);

            // Afegim les dates de cada dia de la setmana anterior a la llista
            for (int i = 0; i < 7; i++)
            {
                DateTime data = primerDiaDeLaSetmanaAnterior.AddDays(i);
                string dataFormatada = data.ToString("dd/MM/yyyy");
                string nomDelDia = data.ToString("dddd", new CultureInfo("ca-ES"));
                nomDelDia = char.ToUpper(nomDelDia[0]) + nomDelDia.Substring(1);
                dates.Add(nomDelDia + "\n" + dataFormatada);
            }

            return dates;
        }

        public static List<string> ObtenirDatesDeLaSetmanaSeguent()
        {
            List<string> dates = new List<string>();

            // Obtenim la data actual y el dia de la setmana actual
            DateTime dataActual = DateTime.Today;
            DayOfWeek diaActual = dataActual.DayOfWeek;

            // Calculem el primer dia de la setmana seguent
            DateTime primerDiaDeLaSetmanaSeguent = dataActual.AddDays(-(int)diaActual + 8);

            // Afegim les dates de cada dia de la setmana seguent a la llista
            for (int i = 0; i < 7; i++)
            {
                DateTime data = primerDiaDeLaSetmanaSeguent.AddDays(i);
                string dataFormatada = data.ToString("dd/MM/yyyy");
                string nomDelDia = data.ToString("dddd", new CultureInfo("ca-ES"));
                nomDelDia = char.ToUpper(nomDelDia[0]) + nomDelDia.Substring(1);
                dates.Add(nomDelDia + "\n" + dataFormatada);
            }

            return dates;
        }

        public static string CalcularEdat(DateTime dataNaix)
        {
            int edat = DateTime.Today.Year - dataNaix.Year;

            // Restar un año si aún no ha pasado el cumpleaños de este año
            if (dataNaix > DateTime.Today.AddYears(-edat))
                edat--;

            return edat.ToString();
        }

        public static bool Ultimes48Horas(DateTime data)
        {
            DateTime ara = DateTime.Now;
            DateTime iniciRang = ara.AddDays(-2).Date; // Inici del rang: fa dos dies a las 00:00
            DateTime fiRang = ara.Date.AddDays(1).AddTicks(-1); // Fi del rang: avui a las 23:59:59.9999999

            if (data >= iniciRang && data <= fiRang)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
