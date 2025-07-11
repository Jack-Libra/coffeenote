package com.coffeenote.api.service;

import com.coffeenote.api.model.Note;
import com.coffeenote.api.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Note 服務測試
 */
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note testNote;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testNote = new Note();
        testNote.setId(1L);
        testNote.setBeanName("Ethiopian Yirgacheffe");
        testNote.setOrigin("Ethiopia");
        testNote.setRoastLevel("Medium");
        testNote.setFlavorNotes("Floral, citrus, bright acidity");
        testNote.setRating(5);
        testNote.setBrewingMethod("Pour Over");
        testNote.setUserId(testUserId);
        testNote.setCreatedAt(LocalDateTime.now());
        testNote.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetNotesByUserId() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Note> notes = Arrays.asList(testNote);
        Page<Note> notePage = new PageImpl<>(notes, pageable, 1);
        
        when(noteRepository.findByUserId(testUserId, pageable)).thenReturn(notePage);

        // When
        Page<Note> result = noteService.getNotesByUserId(testUserId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testNote.getBeanName(), result.getContent().get(0).getBeanName());
        verify(noteRepository).findByUserId(testUserId, pageable);
    }

    @Test
    void testGetAllNotesByUserId() {
        // Given
        List<Note> notes = Arrays.asList(testNote);
        when(noteRepository.findByUserId(testUserId)).thenReturn(notes);

        // When
        List<Note> result = noteService.getAllNotesByUserId(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testNote.getBeanName(), result.get(0).getBeanName());
        verify(noteRepository).findByUserId(testUserId);
    }

    @Test
    void testGetNoteByIdAndUserId() {
        // Given
        Long noteId = 1L;
        when(noteRepository.findByIdAndUserId(noteId, testUserId)).thenReturn(Optional.of(testNote));

        // When
        Note result = noteService.getNoteByIdAndUserId(noteId, testUserId);

        // Then
        assertNotNull(result);
        assertEquals(testNote.getBeanName(), result.getBeanName());
        verify(noteRepository).findByIdAndUserId(noteId, testUserId);
    }

    @Test
    void testGetNoteByIdAndUserIdNotFound() {
        // Given
        Long noteId = 999L;
        when(noteRepository.findByIdAndUserId(noteId, testUserId)).thenReturn(Optional.empty());

        // When
        Note result = noteService.getNoteByIdAndUserId(noteId, testUserId);

        // Then
        assertNull(result);
        verify(noteRepository).findByIdAndUserId(noteId, testUserId);
    }

    @Test
    void testCreateNote() {
        // Given
        Note newNote = new Note();
        newNote.setBeanName("Colombian Supremo");
        newNote.setUserId(testUserId);
        
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        // When
        Note result = noteService.createNote(newNote);

        // Then
        assertNotNull(result);
        assertNull(newNote.getId()); // ID should be set to null before saving
        verify(noteRepository).save(newNote);
    }

    @Test
    void testUpdateNote() {
        // Given
        when(noteRepository.existsByIdAndUserId(testNote.getId(), testNote.getUserId())).thenReturn(true);
        when(noteRepository.save(testNote)).thenReturn(testNote);

        // When
        Note result = noteService.updateNote(testNote);

        // Then
        assertNotNull(result);
        assertEquals(testNote.getBeanName(), result.getBeanName());
        verify(noteRepository).existsByIdAndUserId(testNote.getId(), testNote.getUserId());
        verify(noteRepository).save(testNote);
    }

    @Test
    void testUpdateNoteNotOwned() {
        // Given
        when(noteRepository.existsByIdAndUserId(testNote.getId(), testNote.getUserId())).thenReturn(false);

        // When
        Note result = noteService.updateNote(testNote);

        // Then
        assertNull(result);
        verify(noteRepository).existsByIdAndUserId(testNote.getId(), testNote.getUserId());
        verify(noteRepository, never()).save(any());
    }

    @Test
    void testDeleteNoteByIdAndUserId() {
        // Given
        Long noteId = 1L;
        when(noteRepository.findByIdAndUserId(noteId, testUserId)).thenReturn(Optional.of(testNote));

        // When
        boolean result = noteService.deleteNoteByIdAndUserId(noteId, testUserId);

        // Then
        assertTrue(result);
        verify(noteRepository).findByIdAndUserId(noteId, testUserId);
        verify(noteRepository).delete(testNote);
    }

    @Test
    void testDeleteNoteByIdAndUserIdNotFound() {
        // Given
        Long noteId = 999L;
        when(noteRepository.findByIdAndUserId(noteId, testUserId)).thenReturn(Optional.empty());

        // When
        boolean result = noteService.deleteNoteByIdAndUserId(noteId, testUserId);

        // Then
        assertFalse(result);
        verify(noteRepository).findByIdAndUserId(noteId, testUserId);
        verify(noteRepository, never()).delete(any());
    }

    @Test
    void testSearchNotes() {
        // Given
        String keyword = "Ethiopian";
        List<Note> notes = Arrays.asList(testNote);
        when(noteRepository.searchNotes(testUserId, keyword)).thenReturn(notes);

        // When
        List<Note> result = noteService.searchNotes(testUserId, keyword);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(noteRepository).searchNotes(testUserId, keyword);
    }

    @Test
    void testSearchNotesEmptyKeyword() {
        // Given
        String keyword = "";
        List<Note> notes = Arrays.asList(testNote);
        when(noteRepository.findByUserId(testUserId)).thenReturn(notes);

        // When
        List<Note> result = noteService.searchNotes(testUserId, keyword);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(noteRepository).findByUserId(testUserId);
        verify(noteRepository, never()).searchNotes(any(), any());
    }

    @Test
    void testGetNoteStats() {
        // Given
        when(noteRepository.countByUserId(testUserId)).thenReturn(5L);
        when(noteRepository.findAverageRatingByUserId(testUserId)).thenReturn(4.2);
        when(noteRepository.countByUserIdAndRating(testUserId, 1)).thenReturn(0L);
        when(noteRepository.countByUserIdAndRating(testUserId, 2)).thenReturn(1L);
        when(noteRepository.countByUserIdAndRating(testUserId, 3)).thenReturn(1L);
        when(noteRepository.countByUserIdAndRating(testUserId, 4)).thenReturn(1L);
        when(noteRepository.countByUserIdAndRating(testUserId, 5)).thenReturn(2L);

        // When
        Map<String, Object> result = noteService.getNoteStats(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(5L, result.get("totalNotes"));
        assertEquals(4.2, result.get("averageRating"));
        assertEquals(2L, result.get("highRatedNotes"));
        assertEquals(0L, result.get("lowRatedNotes"));
        
        @SuppressWarnings("unchecked")
        Map<Integer, Long> ratingCounts = (Map<Integer, Long>) result.get("ratingCounts");
        assertNotNull(ratingCounts);
        assertEquals(5, ratingCounts.size());
    }

    @Test
    void testIsNoteOwnedByUser() {
        // Given
        Long noteId = 1L;
        when(noteRepository.existsByIdAndUserId(noteId, testUserId)).thenReturn(true);

        // When
        boolean result = noteService.isNoteOwnedByUser(noteId, testUserId);

        // Then
        assertTrue(result);
        verify(noteRepository).existsByIdAndUserId(noteId, testUserId);
    }

    @Test
    void testDeleteAllNotesByUserId() {
        // Given
        when(noteRepository.deleteByUserId(testUserId)).thenReturn(3L);

        // When
        long result = noteService.deleteAllNotesByUserId(testUserId);

        // Then
        assertEquals(3L, result);
        verify(noteRepository).deleteByUserId(testUserId);
    }
}
