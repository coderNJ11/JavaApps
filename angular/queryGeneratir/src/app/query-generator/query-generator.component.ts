// query-generator.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class QueryGeneratorService {

  private operators = {
    '=': (a, b) => this.compareValues(a, b, 'eq'),
    '<': (a, b) => this.compareValues(a, b, 'lt'),
    '>': (a, b) => this.compareValues(a, b, 'gt'),
    '<=': (a, b) => this.compareValues(a, b, 'lte'),
    '>=': (a, b) => this.compareValues(a, b, 'gte'),
    '!=': (a, b) => this.compareValues(a, b, 'neq')
  };

  private typeMap = {
    string: 'string',
    number: 'number',
    boolean: 'boolean',
    date: 'date',
    array: 'array',
    object: 'object'
  };

  compareValues(a: any, b: any, operator: string): boolean {
    const typeA = this.getType(a);
    const typeB = this.getType(b);

    if (typeA !== typeB) {
      throw new Error(`Cannot compare values of different types: ${typeA} and ${typeB}`);
    }

    switch (typeA) {
      case 'string':
        return this.compareStrings(a, b, operator);
      case 'number':
        return this.compareNumbers(a, b, operator);
      case 'boolean':
        return this.compareBooleans(a, b, operator);
      case 'date':
        return this.compareDates(a, b, operator);
      case 'array':
        return this.compareArrays(a, b, operator);
      case 'object':
        return this.compareObjects(a, b, operator);
      default:
        throw new Error(`Unsupported type: ${typeA}`);
    }
  }

  private compareStrings(a: string, b: string, operator: string): boolean {
    switch (operator) {
      case 'eq':
        return a === b;
      case 'neq':
        return a !== b;
      default:
        throw new Error(`Unsupported operator for strings: ${operator}`);
    }
  }

  private compareNumbers(a: number, b: number, operator: string): boolean {
    switch (operator) {
      case 'eq':
        return a === b;
      case 'neq':
        return a !== b;
      case 'lt':
        return a < b;
      case 'gt':
        return a > b;
      case 'lte':
        return a <= b;
      case 'gte':
        return a >= b;
      default:
        throw new Error(`Unsupported operator for numbers: ${operator}`);
    }
  }

  private compareBooleans(a: boolean, b: boolean, operator: string): boolean {
    switch (operator) {
      case 'eq':
        return a === b;
      case 'neq':
        return a !== b;
      default:
        throw new Error(`Unsupported operator for booleans: ${operator}`);
    }
  }

  private compareDates(a: Date, b: Date, operator: string): boolean {
    switch (operator) {
      case 'eq':
        return a.getTime() === b.getTime();
      case 'neq':
        return a.getTime() !== b.getTime();
      case 'lt':
        return a.getTime() < b.getTime();
      case 'gt':
        return a.getTime() > b.getTime();
      case 'lte':
        return a.getTime() <= b.getTime();
      case 'gte':
        return a.getTime() >= b.getTime();
      default:
        throw new Error(`Unsupported operator for dates: ${operator}`);
    }
  }

  private compareArrays(a: any[], b: any[], operator: string): boolean {
    switch (operator) {
      case 'eq':
        return JSON.stringify(a) === JSON.stringify(b);
      case 'neq':
        return JSON.stringify(a) !== JSON.stringify(b);
      default:
        throw new Error(`Unsupported operator for arrays: ${operator}`);
    }
  }

  private compareObjects(a: any, b: any, operator: string): boolean {
    switch (operator) {
      case 'eq':
        return JSON.stringify(a) === JSON.stringify(b);
      case 'neq':
        return JSON.stringify(a) !== JSON.stringify(b);
      default:
        throw new Error(`Unsupported operator for objects: ${operator}`);
    }
  }

  private getType(value: any): string {
    return this.typeMap[typeof value] || 'object';
  }

  generateQuery(entity1: any, entity2: any, operator: string, value: any): string {
    const operatorFn = this.operators[operator];
    if (!operatorFn) {
      throw new Error(`Invalid operator: ${operator}`);
    }

    const entity1Key = Object.keys(entity1)[0];
    const entity2Key = Object.keys(entity2)[0];

    const query = `(${entity1Key} ${operator} ${value})`;
    if (entity2) {
      query += ` AND (${entity2Key} ${operator} ${value})`;
    }

    return query;
  }

  generateIfElseQuery(entity1: any, entity2: any, operator: string, value: any, ifTrue: string, ifFalse: string): string {
    const query = this.generateQuery(entity1, entity2, operator, value);
    return `IF(${query}, ${ifTrue}, ${ifFalse})`;
  }
}
