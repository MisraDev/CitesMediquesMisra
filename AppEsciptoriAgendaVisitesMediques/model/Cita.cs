using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AppEsciptoriAgendaVisitesMediques.model
{
    class Cita
    {
        private int codiCita;
       
        private DateTime dataCita;

        private String informe;
        
        private Persona persona;
        
        private Persona metge;

        //private Especialitats especialitat;

        public Cita(int codiCíta, DateTime dataCita, String infrome, Persona persona, Persona metge)
        {
            this.codiCita = codiCíta;
            this.dataCita = dataCita;
            this.informe = infrome;
            this.persona = persona;
            this.metge = metge;
        }

        public int CodiCita
        {
            get { return codiCita; }
            set { codiCita = value; }
        }

        public DateTime DataCita
        {
            get { return dataCita; }
            set { dataCita = value; }
        }

        public String Informe
        {
            get { return informe; }
            set { informe = value; }
        }

        public Persona Persona
        {
            get { return persona; }
            set { persona = value; }
        }

        public Persona Metge
        {
            get { return metge; }
            set { metge = value; }
        }

        //public Especialitats Especialitat
        //{
        //    get { return especialitat; }
        //    set { especialitat = value; }
        //}

        public override int GetHashCode()
        {
            return codiCita.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            if (obj == null || GetType() != obj.GetType())
            {
                return false;
            }

            Cita other = (Cita)obj;
            return codiCita == other.codiCita;
        }


    }
}
