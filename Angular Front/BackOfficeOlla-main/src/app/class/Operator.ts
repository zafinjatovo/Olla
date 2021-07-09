
export class Operator
{
  public id:number = 0;
  public name:string;
  public preffix:string;

  constructor(name : string, preffix : string, id ? : number)
  {
    this.name = name;
    this.preffix = preffix;
    if(id != null)
    {
      this.id = id;
    }
  }
}
