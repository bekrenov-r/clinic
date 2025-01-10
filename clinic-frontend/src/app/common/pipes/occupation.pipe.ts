import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'occupation'
})
export class OccupationPipe implements PipeTransform {
  transform(occupation: string): string {
    if(!occupation) {
      return '';
    }
    const words: string[] = occupation.split('_').map(s => s.toLowerCase());
    words[0] = words[0].charAt(0).toUpperCase() + words[0].slice(1);
    return words.join(' ');
  }
}
