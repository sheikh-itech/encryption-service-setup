import { KeyValue } from "@angular/common";
import { DatePipe } from '@angular/common';
import { Injectable } from "@angular/core";


@Injectable({ providedIn: 'root' })
class Utility {

  constructor(private datePipe: DatePipe) {

  }

  public static originalOrder(firstVal: KeyValue<number, unknown>, secondVal: KeyValue<number, unknown>): number {
    return 0;
  }

  public static checkObjectsEquality(firstObject: any, secondObject: any): boolean {

    let outcome = true;

    if (Object.keys(firstObject).length != Object.keys(secondObject).length)
      return false;    

    Object.keys(firstObject).forEach(key => {
      if (firstObject[key] != secondObject[key])
        outcome = false;
    });

    return outcome;
  }

  public yyyyMM(dateString: string): string {

    return String(this.datePipe.transform(dateString, 'yyyy-MM'));
  }

  public yyyyMMdd(dateString: string): string {

    return String(this.datePipe.transform(dateString, 'yyyy-MM-dd'));
  }

  public ddMMMyyyy(dateString: string): string {

    return String(this.datePipe.transform(dateString, 'dd-MMM-yyyy'));
  }

  public ddMMMyyyyHMS(dateString: string): string {

    return String(this.datePipe.transform(dateString, 'dd-MM-yyyy h:m:s'));
  }

  public yyyyMMddHMS(dateString: string): string {

    return String(this.datePipe.transform(dateString, 'yyyy-MM-dd h:m:s'));
  }

  public mmmYY(dateString: string): string {

    return String(this.datePipe.transform(dateString, 'MMM yy'));
  }
}

export { Utility }
