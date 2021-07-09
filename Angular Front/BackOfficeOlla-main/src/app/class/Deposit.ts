import { User } from './User';

export class Deposit
{
    public id :number = 0;
    public user : User;
    public cash : number;
    public transaction : string;
    public date : Date;

    constructor(user: User, cash: number, transaction: string, date: string, id?: number)
    {
        if(id != null)
        {
            this.id = id;
        }
        this.user = user;
        this.cash = cash;
        this.transaction = transaction;
        this.date = new Date(date);
    }
}
