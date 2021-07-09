export class User
{
    public id :number = 0;
    public idOperator : number;
    public name : string;
    public firstname : string;
    public email : string;
    public password : string;
    public phonenumber :string;
    public ncin : string;

    constructor(idOperator : number, name : string, firstname : string, email : string, password : string, phonenumber : string, ncin : string, id? : number)
    {
      if(id != null)
      {
        this.id = id;
      }
      this.idOperator = idOperator;
      this.name = name;
      this.firstname = firstname;
      this.email = email;
      this.password = password;
      this.phonenumber = phonenumber;
      this.ncin = ncin;
    }
}
