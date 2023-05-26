using DbLibrary.modelo;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Media;

namespace AppEscripotoriGestióHorari
{
    public class EspecialidadToColorConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, string language)
        {
            EspecialitatDB especialitatObject = value as EspecialitatDB;

            string especialidad = especialitatObject.NomEspecialitat;
           
                
            if (!string.IsNullOrEmpty(especialidad))
            {
                // Asigna el color según el nombre de la especialidad
                if (especialidad == "Cardiología")
                {
                    return new SolidColorBrush(Colors.LightBlue);
                }
                else if (especialidad == "Traumatología")
                {
                    return new SolidColorBrush(Colors.LightCyan);
                }
                else if (especialidad == "Pediatría")
                {
                    return new SolidColorBrush(Colors.LightCoral);
                }
                else if (especialidad == "Oncología")
                {
                    return new SolidColorBrush(Colors.LightGoldenrodYellow);
                }
                else if (especialidad == "Neurología")
                {
                    return new SolidColorBrush(Colors.LightSteelBlue);
                }
                else if (especialidad == "Endocrinología")
                {
                    return new SolidColorBrush(Colors.OldLace);
                }
                else if (especialidad == "Oftalmología")
                {
                    return new SolidColorBrush(Colors.Lime);
                }
                else if (especialidad == "Ginecología")
                {
                    return new SolidColorBrush(Colors.Linen);
                }
                else if (especialidad == "Urología")
                {
                    return new SolidColorBrush(Colors.LightSeaGreen);
                }
                else if (especialidad == "Dermatología")
                {
                    return new SolidColorBrush(Colors.LightYellow);
                }

            }
            
            

            


            // Color predeterminado si no se encuentra ninguna coincidencia
            return new SolidColorBrush(Colors.LightGray);
        }

        public object ConvertBack(object value, Type targetType, object parameter, string language)
        {
            throw new NotImplementedException();
        }
    }
}
