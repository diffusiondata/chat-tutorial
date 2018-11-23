import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatComponent } from './chat.component';
import { template } from '@angular/core/src/render3';

describe('ChatComponent', () => {
  let component: ChatComponent;
  let fixture: ComponentFixture<ChatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ChatComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain properties', () => {
    expect(component.chatLog).toBeDefined();
  });

  it('should contain an input', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('input')).toBeDefined();
  });

  it('should contain a list', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('ul')).toBeDefined();
  });
});
