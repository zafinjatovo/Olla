export class type
{
    public id : number = 0;
    public name : string;
    public unit : string;

    constructor(name : string, unit : string, id ? : number)
    {
      if(id != null)
      {
        this.id = id;
      }
      this.name = name;
      this.unit = unit;
    }
}
